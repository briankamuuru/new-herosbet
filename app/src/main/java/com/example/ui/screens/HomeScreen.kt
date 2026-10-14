package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HistoryCategory
import com.example.ui.TipCategory
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenAccent
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenLight
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.WonGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    isVipUnlocked: Boolean,
    onCategoryClick: (TipCategory) -> Unit,
    onHistoryClick: (HistoryCategory) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isSideDrawerHistoryOpen by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = GreenDark,
                modifier = Modifier.width(300.dp)
            ) {
                // Drawer Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(GreenPrimary, GreenDark)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(GoldAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Logo",
                                tint = GreenDark,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "H E R O S B E T",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                        )
                        Text(
                            text = "Sure Football Predictions & VIP",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFC8E6C9),
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                HorizontalDivider(color = Color(0x33FFFFFF))

                Spacer(modifier = Modifier.height(8.dp))

                // HISTORY DRAWER IN SIDEBAR
                val sideArrowRotation by animateFloatAsState(
                    targetValue = if (isSideDrawerHistoryOpen) 180f else 0f,
                    label = "sideArrow"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x22FFFFFF))
                        .clickable { isSideDrawerHistoryOpen = !isSideDrawerHistoryOpen }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History",
                                tint = GoldAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "HISTORY",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = GoldAccent,
                            modifier = Modifier.rotate(sideArrowRotation)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isSideDrawerHistoryOpen,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        HistoryCategory.entries.forEach { history ->
                            NavigationDrawerItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = history.title,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = history.title,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = Color.White,
                                            fontSize = 14.sp
                                        )
                                    )
                                },
                                selected = false,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    onHistoryClick(history)
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent
                                ),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color(0x33FFFFFF), modifier = Modifier.padding(vertical = 8.dp))

                // Privacy Policy item
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Policy,
                            contentDescription = "Privacy Policy",
                            tint = Color.White
                        )
                    },
                    label = {
                        Text(
                            text = "Privacy Policy",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White
                            )
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onPrivacyPolicyClick()
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                        .testTag("drawer_privacy_policy")
                )

                // Join Telegram
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Join Telegram",
                            tint = Color.White
                        )
                    },
                    label = {
                        Text(
                            text = "Join Telegram",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White
                            )
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/herosvipking"))
                        context.startActivity(intent)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                        .testTag("drawer_telegram")
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "H E R O S B E T",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 3.sp
                            ),
                            modifier = Modifier.testTag("home_title")
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("drawer_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Drawer",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onRefresh,
                            modifier = Modifier.testTag("home_refresh_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = GreenDark
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/herosvipking"))
                        context.startActivity(intent)
                    },
                    containerColor = Color(0xFF0088CC),
                    contentColor = Color.White,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                    modifier = Modifier.testTag("floating_telegram_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Official Telegram",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            modifier = modifier
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(GreenLight)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. DAILY SURE TIPS (ABOVE VIP SECTION)
                    item {
                        DailySureTipsCard(
                            category = TipCategory.DAILY_FREE,
                            onClick = { onCategoryClick(TipCategory.DAILY_FREE) }
                        )
                    }

                    // 2. VIP SECTION
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            // Row: 3 ODDS VIP & 6 ODDS VIP
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryGridItem(
                                        category = TipCategory.VIP_3_ODDS,
                                        isVipUnlocked = isVipUnlocked,
                                        onClick = { onCategoryClick(TipCategory.VIP_3_ODDS) }
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryGridItem(
                                        category = TipCategory.VIP_6_ODDS,
                                        isVipUnlocked = isVipUnlocked,
                                        onClick = { onCategoryClick(TipCategory.VIP_6_ODDS) }
                                    )
                                }
                            }

                            // Maxbetters Special VIP
                            CategoryGridItem(
                                category = TipCategory.VIP_MAXBET,
                                isVipUnlocked = isVipUnlocked,
                                onClick = { onCategoryClick(TipCategory.VIP_MAXBET) },
                                isFullWidth = true
                            )
                        }
                    }

                    // 3. HISTORY SECTION: DRAWER BELOW THE VIP TABS
                    item {
                        HistoryAccordionDrawer(
                            onHistoryClick = onHistoryClick
                        )
                    }
                }
            }
        }
    }
}

// THE MAIN HISTORY DRAWER LOCATED DIRECTLY BELOW THE VIP TABS
// CONTAINS THREE SUBSECTION DRAWERS FOR EACH VIP SECTION
@Composable
fun HistoryAccordionDrawer(
    onHistoryClick: (HistoryCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMainDrawerOpen by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (isMainDrawerOpen) 180f else 0f,
        label = "arrowRotation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(24.dp), spotColor = GoldAccent)
            .border(2.dp, GoldAccent, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .testTag("history_main_drawer"),
        colors = CardDefaults.cardColors(containerColor = GreenDark)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Main History Drawer Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isMainDrawerOpen = !isMainDrawerOpen }
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x33000000))
                            .border(1.dp, GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "HISTORY",
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "HISTORY",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isMainDrawerOpen) "Collapse" else "Expand",
                    tint = GoldAccent,
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(arrowRotation)
                )
            }

            // Expanded content: Three subsection drawers for each VIP section
            AnimatedVisibility(
                visible = isMainDrawerOpen,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = Color(0x33FFFFFF))

                    // Subsection Drawer 1: 3 ODDS VIP History
                    VipSectionSubsectionDrawer(
                        title = "3 ODDS VIP HISTORY",
                        oddsTag = "3.00+ Odds",
                        subtitle = "Verified winning 3 ODDS tickets",
                        onClick = { onHistoryClick(HistoryCategory.HIST_3_ODDS) },
                        testTag = "history_subsection_3odds"
                    )

                    // Subsection Drawer 2: 6 ODDS VIP History
                    VipSectionSubsectionDrawer(
                        title = "6 ODDS VIP HISTORY",
                        oddsTag = "6.00+ Odds",
                        subtitle = "Verified winning 6 ODDS combo tickets",
                        onClick = { onHistoryClick(HistoryCategory.HIST_6_ODDS) },
                        testTag = "history_subsection_6odds"
                    )

                    // Subsection Drawer 3: Maxbetters Special VIP History
                    VipSectionSubsectionDrawer(
                        title = "MAXBETTERS SPECIAL VIP HISTORY",
                        oddsTag = "High Stakes",
                        subtitle = "Verified winning special VIP tickets",
                        onClick = { onHistoryClick(HistoryCategory.HIST_SPECIAL) },
                        testTag = "history_subsection_special"
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

// INDIVIDUAL SUBSECTION DRAWER FOR EACH VIP SECTION
@Composable
fun VipSectionSubsectionDrawer(
    title: String,
    oddsTag: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GoldAccent.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = GreenPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x33000000))
                            .border(0.5.dp, GoldAccent, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = oddsTag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFC8E6C9),
                        fontSize = 12.sp
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(GoldAccent)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "VIEW",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = GreenDark,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
fun DailySureTipsCard(
    category: TipCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color.White
            )
            .border(
                width = 1.5.dp,
                color = Color.White.copy(alpha = 0.7f),
                shape = RoundedCornerShape(28.dp)
            )
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .testTag("category_card_${category.id}"),
        colors = CardDefaults.cardColors(containerColor = GreenAccent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Daily Sure Tips",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "VIEW",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = GreenDark,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}

@Composable
fun CategoryGridItem(
    category: TipCategory,
    isVipUnlocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFullWidth: Boolean = false
) {
    val isFree = !category.isVip
    val backgroundColor = if (isFree) GreenAccent else GreenPrimary
    val borderColor = if (isFree) Color.White.copy(alpha = 0.6f) else GoldAccent

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isFullWidth) 115.dp else 155.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = if (isFree) Color.White else GoldAccent
            )
            .border(
                width = if (isFree) 1.5.dp else 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(28.dp)
            )
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .testTag("category_card_${category.id}"),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        if (isFullWidth) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (isVipUnlocked) WonGreen else Color(0x33000000))
                            .border(1.dp, GoldAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isVipUnlocked) Icons.Default.Check else Icons.Default.Lock,
                            contentDescription = if (isVipUnlocked) "Unlocked" else "VIP",
                            tint = GoldAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GoldAccent)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isVipUnlocked) "VIEW" else "VIP",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = GreenDark,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Category Icon / Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (category.isVip) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isVipUnlocked) WonGreen else Color(0x33000000))
                                .padding(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isVipUnlocked) Icons.Default.Check else Icons.Default.Lock,
                                contentDescription = if (isVipUnlocked) "Unlocked" else "VIP",
                                tint = GoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Free",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (category.title.length > 15) 17.sp else 20.sp,
                            lineHeight = 22.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
