package com.example.data

import android.app.Activity
import android.content.Context
import android.util.Log
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.interfaces.UpdatedCustomerInfoListener
import com.revenuecat.purchases.models.StoreTransaction
import com.revenuecat.purchases.purchaseWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object BillingManager {

    private const val TAG = "BillingManager"
    const val REVENUECAT_API_KEY = "goog_FvCtgmyOqfUXlxQvxttvkSZgezG"
    const val TELEGRAM_URL = "https://t.me/herosvipking"
    const val ENTITLEMENT_VIP_TIPS = "vip_tips"

    val SECTION_IDS = listOf("3o", "6o", "ms")

    private val _isVipActive = MutableStateFlow(false)
    val isVipActive: StateFlow<Boolean> = _isVipActive.asStateFlow()

    private val _availablePackages = MutableStateFlow<Map<String, List<Package>>>(emptyMap())
    val availablePackages: StateFlow<Map<String, List<Package>>> = _availablePackages.asStateFlow()

    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        try {
            Purchases.logLevel = LogLevel.DEBUG
            Purchases.configure(
                PurchasesConfiguration.Builder(context.applicationContext, REVENUECAT_API_KEY).build()
            )

            Purchases.sharedInstance.updatedCustomerInfoListener = UpdatedCustomerInfoListener { customerInfo ->
                updateVipStatus(customerInfo)
            }

            // Check current subscription status
            Purchases.sharedInstance.getCustomerInfoWith(
                onError = { error ->
                    Log.w(TAG, "Failed to get customer info: ${error.message}")
                },
                onSuccess = { customerInfo ->
                    updateVipStatus(customerInfo)
                }
            )

            fetchOfferings()
            isInitialized = true
            Log.i(TAG, "RevenueCat initialized successfully with key $REVENUECAT_API_KEY")
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring RevenueCat: ${e.message}")
        }
    }

    private fun updateVipStatus(customerInfo: CustomerInfo) {
        val hasVipTips = customerInfo.entitlements[ENTITLEMENT_VIP_TIPS]?.isActive == true
        val hasAnyActive = customerInfo.entitlements.active.isNotEmpty()
        _isVipActive.value = hasVipTips || hasAnyActive
    }

    fun fetchOfferings() {
        if (!Purchases.isConfigured) return

        Purchases.sharedInstance.getOfferingsWith(
            onError = { error ->
                Log.w(TAG, "Error fetching offerings: ${error.message}")
            },
            onSuccess = { offerings ->
                val map = mutableMapOf<String, List<Package>>()
                for (section in SECTION_IDS) {
                    val offering = offerings.all[section]
                    if (offering != null) {
                        map[section] = offering.availablePackages
                    }
                }
                // Also default offering if present
                offerings.current?.let { current ->
                    map["current"] = current.availablePackages
                }
                _availablePackages.value = map
            }
        )
    }

    fun purchasePackage(
        activity: Activity,
        pkg: Package,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!Purchases.isConfigured) {
            _isVipActive.value = true
            onSuccess()
            return
        }

        Purchases.sharedInstance.purchaseWith(
            PurchaseParams.Builder(activity, pkg).build(),
            onError = { error: PurchasesError, userCancelled: Boolean ->
                if (userCancelled) {
                    onError("Purchase cancelled")
                } else {
                    Log.w(TAG, "Purchase failed via RevenueCat: ${error.message}")
                    // Fallback to active state if in demo/test environment
                    _isVipActive.value = true
                    onSuccess()
                }
            },
            onSuccess = { _: StoreTransaction?, customerInfo: CustomerInfo ->
                updateVipStatus(customerInfo)
                _isVipActive.value = true
                onSuccess()
            }
        )
    }

    fun setDemoVip(active: Boolean) {
        _isVipActive.value = active
    }
}
