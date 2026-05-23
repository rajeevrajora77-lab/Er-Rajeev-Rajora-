package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PortfolioRepository
import com.example.data.model.SkillCategory
import com.example.data.model.SkillItem
import com.example.ui.components.IconHelper
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

@Composable
fun SkillsEcosystemScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedCategoryIndex by remember { mutableStateOf(0) }

    // Constellation rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "ConstellationRotation")
    val angleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    val pulsePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- PAGE HEADER ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Skill Galaxy & Clusters",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Interactive constellation showing Rajeev's core operational capabilities.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // --- SECTION A: CUSTOM ANIMATED SKILL CONSTELLATION ---
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "RAJORA SYSTEM NODE MAP (CONCISE)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val centerX = size.width / 2
                        val centerY = size.height / 2
                        val baseRadius = size.minDimension / 3.2f

                        // Draw background orbital ranges
                        drawCircle(
                            color = Color.DarkGray,
                            radius = baseRadius,
                            center = Offset(centerX, centerY),
                            style = Stroke(width = 3f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                        )

                        drawCircle(
                            color = Color.DarkGray.copy(alpha = 0.5f),
                            radius = baseRadius * 0.6f,
                            center = Offset(centerX, centerY),
                            style = Stroke(width = 2f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
                        )

                        // Center Hub: "Rajora AI" Core Engine
                        drawCircle(
                            color = NeonCyan.copy(alpha = 0.15f * pulsePulse),
                            radius = 32f * pulsePulse,
                            center = Offset(centerX, centerY)
                        )
                        drawCircle(
                            color = NeonCyan,
                            radius = 12f,
                            center = Offset(centerX, centerY)
                        )

                        // Render 4 major category node centers rotating
                        val categoriesCoords = listOf("Orchestrator", "Quantizer", "Cluster", "Memory")
                        val starColors = listOf(NeonCyan, LightIndigo, MetricGreen, Color(0xFFE0E7FF))

                        categoriesCoords.forEachIndexed { index, label ->
                            val currentAngleRad = Math.toRadians((angleRotation + (index * 90)).toDouble())
                            val nodeRadius = if (index == selectedCategoryIndex) baseRadius * 1.1f else baseRadius
                            val nodeX = (centerX + nodeRadius * cos(currentAngleRad)).toFloat()
                            val nodeY = (centerY + nodeRadius * sin(currentAngleRad)).toFloat()

                            // Line connecting node back to center Hub
                            drawLine(
                                color = if (index == selectedCategoryIndex) NeonCyan.copy(alpha = 0.6f) else Color.DarkGray.copy(alpha = 0.3f),
                                start = Offset(centerX, centerY),
                                end = Offset(nodeX, nodeY),
                                strokeWidth = if (index == selectedCategoryIndex) 2f else 1f
                            )

                            // Nodes halos
                            drawCircle(
                                color = starColors[index].copy(alpha = if (index == selectedCategoryIndex) 0.35f else 0.15f),
                                radius = if (index == selectedCategoryIndex) 22f * pulsePulse else 15f * pulsePulse,
                                center = Offset(nodeX, nodeY)
                            )

                            // Core Node Point
                            drawCircle(
                                color = starColors[index],
                                radius = if (index == selectedCategoryIndex) 8f else 5f,
                                center = Offset(nodeX, nodeY)
                            )
                        }
                    }

                    // Interactive overlay to tell them to tap categories
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Orbit Rotation: Active · Tap Categories to Highlight",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        // --- SECTION B: CATEGORY SELECTOR CAROUSEL ---
        Text(
            text = "SELECT CAPABILITY CORE CLUSTER:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PortfolioRepository.skillCategories.forEachIndexed { index, category ->
                val isSelected = index == selectedCategoryIndex
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedCategoryIndex = index }
                        .testTag("skill_category_card_$index")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = IconHelper.getIconByName(category.iconName),
                            contentDescription = category.categoryName,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = category.categoryName.split(" ").first(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // --- SECTION C: ACTIVE SKILLS BREAKDOWNS ---
        val activeCategory = PortfolioRepository.skillCategories[selectedCategoryIndex]

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = IconHelper.getIconByName(activeCategory.iconName),
                        contentDescription = "Category Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = activeCategory.categoryName,
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                activeCategory.skills.forEach { skill ->
                    SkillProgressRow(skill = skill)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
fun SkillProgressRow(
    skill: SkillItem
) {
    // Progress spring loading trigger
    var triggerProgress by remember { mutableStateOf(false) }

    LaunchedEffect(skill) {
        triggerProgress = false
        delay(100)
        triggerProgress = true
    }

    val progressValue by animateFloatAsState(
        targetValue = if (triggerProgress) skill.level else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow),
        label = "SkillProgress"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = IconHelper.getIconByName(skill.iconName),
                    contentDescription = skill.name,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = skill.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = String.format("%.0f%%", skill.level * 100),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progressValue)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    )
            )
        }
    }
}
