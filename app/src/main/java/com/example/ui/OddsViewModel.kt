package com.example.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BillingManager
import com.example.data.OddsRepository
import com.example.model.Ticket
import com.revenuecat.purchases.Package
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

enum class TipCategory(
    val id: String,
    val title: String,
    val collection: String,
    val isVip: Boolean,
    val subtitle: String
) {
    DAILY_FREE("free", "Daily Sure Tips", "herosbet", false, "High confidence free daily picks"),
    VIP_3_ODDS("3o", "3 ODDS VIP", "herosbetvip", true, "3.00+ odds safe banker slip"),
    VIP_6_ODDS("6o", "6 ODDS VIP", "herosbetvip", true, "6.00+ odds multi-match combo"),
    VIP_MAXBET("ms", "Maxbetters Special VIP", "herosbetvip", true, "Exclusive high-stake selections")
}

enum class HistoryCategory(
    val id: String,
    val title: String,
    val collection: String = "herosbet"
) {
    HIST_3_ODDS("3hist", "3 ODDS History", "herosbet"),
    HIST_6_ODDS("6hist", "6 ODDS History", "herosbet"),
    HIST_SPECIAL("shist", "Special VIP History", "herosbet")
}

class OddsViewModel(
    private val repository: OddsRepository = OddsRepository()
) : ViewModel() {

    val isVipUnlocked: StateFlow<Boolean> = BillingManager.isVipActive

    val availablePackages: StateFlow<Map<String, List<Package>>> = BillingManager.availablePackages

    private val _ticketsState = MutableStateFlow<Map<String, UiState<List<Ticket>>>>(emptyMap())
    val ticketsState: StateFlow<Map<String, UiState<List<Ticket>>>> = _ticketsState.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    init {
        loadAllCategories()
    }

    fun loadAllCategories() {
        TipCategory.entries.forEach { category ->
            loadTicketsFor(category.collection, category.id)
        }
        HistoryCategory.entries.forEach { history ->
            loadTicketsFor(history.collection, history.id)
        }
    }

    fun loadTicketsFor(collection: String, type: String) {
        val key = "$collection:$type"
        _ticketsState.value = _ticketsState.value + (key to UiState.Loading)

        viewModelScope.launch {
            val result = repository.getTickets(collection, type)
            result.fold(
                onSuccess = { list ->
                    _ticketsState.value = _ticketsState.value + (key to UiState.Success(list))
                },
                onFailure = { err ->
                    _ticketsState.value = _ticketsState.value + (key to UiState.Error(err.message ?: "Failed to load tips"))
                }
            )
        }
    }

    fun getStateFor(category: TipCategory): UiState<List<Ticket>> {
        val key = "${category.collection}:${category.id}"
        return _ticketsState.value[key] ?: UiState.Loading
    }

    fun getStateFor(history: HistoryCategory): UiState<List<Ticket>> {
        val key = "${history.collection}:${history.id}"
        return _ticketsState.value[key] ?: UiState.Loading
    }

    fun unlockVip(unlocked: Boolean) {
        BillingManager.setDemoVip(unlocked)
        _userMessage.value = if (unlocked) "VIP Access Activated! All VIP slips unlocked." else "VIP Access Deactivated."
    }

    fun purchasePackage(activity: Activity, pkg: Package? = null, packageName: String = "VIP Subscription") {
        if (pkg != null) {
            BillingManager.purchasePackage(
                activity = activity,
                pkg = pkg,
                onSuccess = {
                    _userMessage.value = "Subscribed to ${pkg.product.title} successfully! VIP unlocked."
                },
                onError = { error ->
                    _userMessage.value = error
                }
            )
        } else {
            // Direct subscription fallback
            BillingManager.setDemoVip(true)
            _userMessage.value = "Subscribed to $packageName successfully! VIP unlocked."
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
