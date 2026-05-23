package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconHelper {
    fun getIconByName(name: String): ImageVector {
        return when (name) {
            "SmartToy" -> Icons.Default.SmartToy
            "Memory" -> Icons.Default.Memory
            "Cloud" -> Icons.Default.Cloud
            "Storage" -> Icons.Default.Storage
            "AccountTree" -> Icons.Default.AccountTree
            "Layers" -> Icons.Default.Layers
            "Search" -> Icons.Default.Search
            "BubbleChart" -> Icons.Default.BubbleChart
            "GraphicEq" -> Icons.Default.GraphicEq
            "CompassCalibration" -> Icons.Default.CompassCalibration
            "Speed" -> Icons.Default.Speed
            "Science" -> Icons.Default.Science
            "Timeline" -> Icons.Default.Timeline
            "Schedule" -> Icons.Default.Schedule
            "ViewQuilt" -> Icons.Default.ViewQuilt
            "CloudQueue" -> Icons.Default.CloudQueue
            "FlashOn" -> Icons.Default.FlashOn
            "Language" -> Icons.Default.Language
            "FactCheck" -> Icons.Default.FactCheck
            "SdStorage" -> Icons.Default.SdStorage
            "Monitoring" -> Icons.Default.QueryStats
            "SettingsEthernet" -> Icons.Default.SettingsEthernet
            "Link" -> Icons.Default.Link
            "Mail" -> Icons.Default.Mail
            "Phone" -> Icons.Default.Phone
            "LocationOn" -> Icons.Default.LocationOn
            "Work" -> Icons.Default.Work
            "School" -> Icons.Default.School
            "Code" -> Icons.Default.Code
            else -> Icons.Default.Star
        }
    }
}
