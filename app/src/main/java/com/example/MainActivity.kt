package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.*
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay

enum class PortfolioTab {
    Overview,
    Products,
    Config,
    Journey,
    Chat
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    var activeTab by remember { mutableStateOf(PortfolioTab.Overview) }
    
    // Server Clock state
    var serverTime by remember { mutableStateOf("UTC 11:19:28") }
    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        while (true) {
            val date = Date()
            serverTime = "UTC " + sdf.format(date)
            delay(1000)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_main_scaffold"),
        topBar = {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "RAJORA AI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = FontFamily.Monospace
                            )
                            
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(6.dp)
                                    .background(Color(0xFF10B981))
                            )
                        }
                        Text(
                            text = "Infrastructure Stack Portfolio",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // System UTC Clock
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = serverTime,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f), thickness = 1.dp)
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .testTag("app_bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == PortfolioTab.Overview,
                    onClick = { activeTab = PortfolioTab.Overview },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Overview", modifier = Modifier.testTag("tab_overview_icon")) },
                    label = { Text("Overview", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = activeTab == PortfolioTab.Products,
                    onClick = { activeTab = PortfolioTab.Products },
                    icon = { Icon(Icons.Default.Science, contentDescription = "Live Products", modifier = Modifier.testTag("tab_products_icon")) },
                    label = { Text("Sandbox", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = activeTab == PortfolioTab.Config,
                    onClick = { activeTab = PortfolioTab.Config },
                    icon = { Icon(Icons.Default.Dns, contentDescription = "Skills ecosystem", modifier = Modifier.testTag("tab_skills_icon")) },
                    label = { Text("Constellation", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = activeTab == PortfolioTab.Journey,
                    onClick = { activeTab = PortfolioTab.Journey },
                    icon = { Icon(Icons.Default.Timeline, contentDescription = "Milestones", modifier = Modifier.testTag("tab_journey_icon")) },
                    label = { Text("Journey", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = activeTab == PortfolioTab.Chat,
                    onClick = { activeTab = PortfolioTab.Chat },
                    icon = { Icon(Icons.Default.SmartToy, contentDescription = "Ask Rajeev AI", modifier = Modifier.testTag("tab_chat_icon")) },
                    label = { Text("Ask AI", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Animate layout changes beautifully with instant fade sweeps
            AnimatedContent(
                targetState = activeTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "ScreenTransition"
            ) { targetTab ->
                when (targetTab) {
                    PortfolioTab.Overview -> HomeOverviewScreen(
                        onNavigateToChat = { activeTab = PortfolioTab.Chat },
                        onNavigateToProducts = { activeTab = PortfolioTab.Products }
                    )
                    PortfolioTab.Products -> LiveProductsScreen()
                    PortfolioTab.Config -> SkillsEcosystemScreen()
                    PortfolioTab.Journey -> CareerTimelineScreen()
                    PortfolioTab.Chat -> AiConsultantChatScreen(
                        onNavigateToTab = { activeTab = it }
                    )
                }
            }
        }
    }
}
