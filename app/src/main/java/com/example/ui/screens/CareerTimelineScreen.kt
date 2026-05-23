package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PortfolioRepository
import com.example.data.model.TimelineItem

@Composable
fun CareerTimelineScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Page Title Header
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Architectural Journey",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Interactive chronicle highlighting major milestones, freelance consultancies, and academics.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // Timeline container
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            PortfolioRepository.timeline.forEachIndexed { index, milestone ->
                val isLast = index == PortfolioRepository.timeline.size - 1
                
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Left Node Column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(36.dp)
                    ) {
                        // Node Circle Indicator
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    if (milestone.isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
                                )
                        ) {
                            Icon(
                                imageVector = if (milestone.isHighlight) Icons.Default.Adjust else Icons.Default.Circle,
                                contentDescription = "Node circle",
                                tint = if (milestone.isHighlight) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(if (milestone.isHighlight) 16.dp else 8.dp)
                            )
                        }

                        // Thread Line
                        if (!isLast) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .fillMaxHeight()
                                    .heightIn(min = 160.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                            )
                                        )
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Right card box content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 24.dp)
                    ) {
                        TimelineItemCard(milestone = milestone, index = index)
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineItemCard(
    milestone: TimelineItem,
    index: Int
) {
    var isExpanded by remember { mutableStateOf(index == 0) } // Default expand the first item

    val cardBorderColor = if (milestone.isHighlight) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
    val cardBgColor = if (milestone.isHighlight) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)

    Card(
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = cardBorderColor, shape = RoundedCornerShape(16.dp))
            .clickable { isExpanded = !isExpanded }
            .testTag("timeline_item_$index")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Meta Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = milestone.period,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Monospace
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (milestone.isHighlight) Icons.Default.WorkspacePremium else Icons.Default.WorkHistory,
                        contentDescription = "Milestone Icon",
                        tint = if (milestone.isHighlight) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = milestone.type,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (milestone.isHighlight) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = milestone.role,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "${milestone.company} · ${milestone.location}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Collapsible accordion arrow indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = if (isExpanded) "Collapse deliverable logs" else "Expand deliverable logs",
                    fontSize = 11.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Arrow indicator",
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                    
                    milestone.bulletPoints.forEach { point ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "• ",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = point,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
