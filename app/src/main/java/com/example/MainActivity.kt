package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.BillingManager
import com.example.ui.HistoryCategory
import com.example.ui.OddsViewModel
import com.example.ui.TipCategory
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.TipsScreen
import com.example.ui.screens.VipPaywallScreen
import com.example.ui.theme.HerosbetTheme
import kotlinx.coroutines.launch

sealed interface Screen {
    data object Home : Screen
    data class Tips(val category: TipCategory) : Screen
    data class HistoryTips(val history: HistoryCategory) : Screen
    data class VipPaywall(val category: TipCategory) : Screen
    data object PrivacyPolicy : Screen
}

class MainActivity : ComponentActivity() {

    private val viewModel: OddsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize billing integration
        BillingManager.init(this)

        setContent {
            HerosbetTheme {
                HerosBetApp(
                    activity = this,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun HerosBetApp(
    activity: Activity,
    viewModel: OddsViewModel,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    val isVipUnlocked by viewModel.isVipUnlocked.collectAsState()
    val packagesMap by viewModel.availablePackages.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { contentPadding ->
        when (val screen = currentScreen) {
            is Screen.Home -> {
                HomeScreen(
                    drawerState = drawerState,
                    isVipUnlocked = isVipUnlocked,
                    onCategoryClick = { category ->
                        if (category.isVip && !isVipUnlocked) {
                            currentScreen = Screen.VipPaywall(category)
                        } else {
                            viewModel.loadTicketsFor(category.collection, category.id)
                            currentScreen = Screen.Tips(category)
                        }
                    },
                    onHistoryClick = { history ->
                        viewModel.loadTicketsFor(history.collection, history.id)
                        currentScreen = Screen.HistoryTips(history)
                    },
                    onPrivacyPolicyClick = {
                        currentScreen = Screen.PrivacyPolicy
                    },
                    onRefresh = {
                        viewModel.loadAllCategories()
                        BillingManager.fetchOfferings()
                        scope.launch {
                            snackbarHostState.showSnackbar("Refreshing latest tips...")
                        }
                    },
                    modifier = Modifier.padding(contentPadding)
                )
            }

            is Screen.Tips -> {
                val uiState = viewModel.getStateFor(screen.category)
                TipsScreen(
                    title = screen.category.title,
                    uiState = uiState,
                    onBack = { currentScreen = Screen.Home },
                    onRefresh = {
                        viewModel.loadTicketsFor(screen.category.collection, screen.category.id)
                    },
                    modifier = Modifier.padding(contentPadding)
                )
            }

            is Screen.HistoryTips -> {
                val uiState = viewModel.getStateFor(screen.history)
                TipsScreen(
                    title = screen.history.title,
                    uiState = uiState,
                    onBack = { currentScreen = Screen.Home },
                    onRefresh = {
                        viewModel.loadTicketsFor(screen.history.collection, screen.history.id)
                    },
                    modifier = Modifier.padding(contentPadding)
                )
            }

            is Screen.VipPaywall -> {
                val sectionPackages = packagesMap[screen.category.id] ?: packagesMap["current"]
                VipPaywallScreen(
                    category = screen.category,
                    revenueCatPackages = sectionPackages,
                    onBack = { currentScreen = Screen.Home },
                    onPurchasePackage = { pkg, packageName ->
                        viewModel.purchasePackage(activity, pkg, packageName)
                        // Transition to the unlocked tips view
                        viewModel.loadTicketsFor(screen.category.collection, screen.category.id)
                        currentScreen = Screen.Tips(screen.category)
                    },
                    modifier = Modifier.padding(contentPadding)
                )
            }

            is Screen.PrivacyPolicy -> {
                PrivacyPolicyScreen(
                    onBack = { currentScreen = Screen.Home },
                    modifier = Modifier.padding(contentPadding)
                )
            }
        }
    }
}
