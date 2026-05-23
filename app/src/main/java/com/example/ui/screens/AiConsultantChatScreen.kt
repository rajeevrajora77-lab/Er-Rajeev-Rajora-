package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.api.GeminiClient
import com.example.data.api.MoshiContent
import com.example.data.api.MoshiPart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

sealed interface ChatUiState {
    object Idle : ChatUiState
    object Loading : ChatUiState
    data class Error(val message: String) : ChatUiState
}

class AiConsultantViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "init",
                text = "Hello! I am Rajeev's AI Digital Clone. Ask me anything about my core specialties in custom fine-tuning, AWS infrastructure design, or how I built AION ∞ and Revive-OS to save cloud expenses! How can I assist you today?",
                isUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(query: String) {
        if (query.isBlank() || _uiState.value is ChatUiState.Loading) return

        val userMessage = ChatMessage(
            id = System.nanoTime().toString(),
            text = query,
            isUser = true
        )

        val updatedMessages = _messages.value + userMessage
        _messages.value = updatedMessages
        _uiState.value = ChatUiState.Loading

        viewModelScope.launch {
            try {
                // Convert list of chat messages into Moshi format history
                val history = updatedMessages.dropLast(1).map { msg ->
                    MoshiContent(parts = listOf(MoshiPart(text = if (msg.isUser) msg.text else msg.text)))
                }

                // Query the Gemini Client
                val response = GeminiClient.askRajeev(query, history)
                
                _messages.value = _messages.value + ChatMessage(
                    id = System.nanoTime().toString(),
                    text = response,
                    isUser = false
                )
                _uiState.value = ChatUiState.Idle
            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.localizedMessage ?: "Unknown Error occurred")
            }
        }
    }

    fun clearHistory() {
        _messages.value = listOf(
            ChatMessage(
                id = "init",
                text = "Hello! I am Rajeev's AI Digital Clone. Ask me anything about my core specialties in custom fine-tuning, AWS infrastructure design, or how I built AION ∞ and Revive-OS to save cloud expenses! How can I assist you today?",
                isUser = false
            )
        )
        _uiState.value = ChatUiState.Idle
    }
}

@Composable
fun AiConsultantChatScreen(
    onNavigateToTab: (com.example.PortfolioTab) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AiConsultantViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var inputQuery by remember { mutableStateOf("") }

    val recommendationChips = listOf(
        "Explain AION's multi-model routing core",
        "Describe Rajora's 3-Tier Search layout",
        "How did you design Revive-OS?",
        "Where did you study academics?"
    )

    // Scroll automatically to bottom of log when a new dialogue arrives
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch {
                lazyListState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- CHAT DIALOGUE HEADER ---
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "AI Agent Avatar",
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Rajeev's Digital Clone",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF10B981).copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF10B981)
                            )
                        }
                    }
                    Text(
                        text = "Answers questions instantly based on Rajeev's professional CV",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- CHAT LOG AREA ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("chat_messages_list")
            ) {
                items(messages, key = { it.id }) { message ->
                    ChatBubble(
                        message = message,
                        onNavigateToTab = onNavigateToTab
                    )
                }

                if (uiState is ChatUiState.Loading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Rajeev AI is thinking...",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- RECOMMENDATION CHIPS SLIDER ---
        AnimatedVisibility(visible = uiState !is ChatUiState.Loading) {
            Column {
                Text(
                    text = "Tap a prompt suggestion:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OptInLayoutAndRow(recommendationChips) { option ->
                    inputQuery = option
                    viewModel.sendMessage(option)
                    focusManager.clearFocus()
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // --- INPUT CHAT BAR ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputQuery,
                onValueChange = { inputQuery = it },
                placeholder = { Text("Ask Rajeev's AI avatar a system query...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (inputQuery.isNotBlank() && uiState !is ChatUiState.Loading) {
                            viewModel.sendMessage(inputQuery)
                            inputQuery = ""
                            focusManager.clearFocus()
                        }
                    }
                ),
                trailingIcon = {
                    if (inputQuery.isNotBlank()) {
                        IconButton(onClick = { inputQuery = "" }) {
                            Text("✕", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            )

            IconButton(
                onClick = {
                    if (inputQuery.isNotBlank() && uiState !is ChatUiState.Loading) {
                        viewModel.sendMessage(inputQuery)
                        inputQuery = ""
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (inputQuery.isNotBlank() && uiState !is ChatUiState.Loading)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)
                    )
                    .testTag("chat_send_button"),
                enabled = inputQuery.isNotBlank() && uiState !is ChatUiState.Loading
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (inputQuery.isNotBlank() && uiState !is ChatUiState.Loading)
                        MaterialTheme.colorScheme.background
                    else
                        Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OptInLayoutAndRow(
    options: List<String>,
    onSelect: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            Text(
                text = option,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelect(option) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    onNavigateToTab: (com.example.PortfolioTab) -> Unit
) {
    val isUser = message.isUser
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val bubbleBg = if (isUser) {
        Brush.horizontalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.primary
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surface
            )
        )
    }

    val bubbleShape = if (isUser) {
        RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp)
    } else {
        RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp)
    }

    val textColor = if (isUser) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurface
    val borderStrokeColor = if (isUser) Color.Transparent else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clip(bubbleShape)
                .background(bubbleBg)
                .border(width = 1.dp, color = borderStrokeColor, shape = bubbleShape)
                .padding(14.dp)
        ) {
            Column {
                if (isUser) {
                    Text(
                        text = message.text,
                        fontSize = 13.sp,
                        color = textColor,
                        lineHeight = 18.sp
                    )
                } else {
                    val (cleanedText, recommendations) = remember(message.text) {
                        parseRecommendations(message.text)
                    }
                    Text(
                        text = cleanedText,
                        fontSize = 13.sp,
                        color = textColor,
                        lineHeight = 18.sp
                    )
                    
                    if (recommendations.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = textColor.copy(alpha = 0.12f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "SUGGESTED ACTIONS:",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor.copy(alpha = 0.5f),
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            recommendations.forEach { rec ->
                                Button(
                                    onClick = {
                                        val tabToNav = if (rec.type == "TAB") {
                                            when (rec.target.lowercase()) {
                                                "products" -> com.example.PortfolioTab.Products
                                                "journey" -> com.example.PortfolioTab.Journey
                                                "config" -> com.example.PortfolioTab.Config
                                                "overview" -> com.example.PortfolioTab.Overview
                                                "chat" -> com.example.PortfolioTab.Chat
                                                else -> null
                                            }
                                        } else {
                                            com.example.PortfolioTab.Products
                                        }
                                        if (tabToNav != null) {
                                            onNavigateToTab(tabToNav)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 32.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (rec.type == "TAB") Icons.Default.Info else Icons.Default.Send,
                                            contentDescription = "Action Icon",
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = rec.label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        Text(
            text = if (isUser) "You" else "Rajeev's Companion",
            fontSize = 9.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 2.dp, start = if (isUser) 0.dp else 4.dp, end = if (isUser) 4.dp else 0.dp)
        )
    }
}

data class ParsedRecommendation(
    val type: String, // "TAB" or "PROJECT"
    val target: String,
    val label: String
)

fun parseRecommendations(rawText: String): Pair<String, List<ParsedRecommendation>> {
    val tabRegex = Regex("\\[RECOMMEND_TAB:([a-zA-Z]+)\\]")
    val projectRegex = Regex("\\[RECOMMEND_PROJECT:([a-zA-Z0-9_-]+)\\]")
    
    val list = mutableListOf<ParsedRecommendation>()
    var cleanedText = rawText
    
    tabRegex.findAll(rawText).forEach { matchResult ->
        val target = matchResult.groupValues[1]
        val label = when (target.lowercase()) {
            "products" -> "Live Sandbox Simulator 🚀"
            "journey" -> "Career Timeline 📅"
            "config" -> "Skills Constellation Map 🌌"
            "overview" -> "Overview Home 🏠"
            else -> "View $target"
        }
        val tabEnum = when (target.lowercase()) {
            "products" -> com.example.PortfolioTab.Products
            "journey" -> com.example.PortfolioTab.Journey
            "config" -> com.example.PortfolioTab.Config
            "overview" -> com.example.PortfolioTab.Overview
            "chat" -> com.example.PortfolioTab.Chat
            else -> null
        }
        if (tabEnum != null) {
            list.add(ParsedRecommendation("TAB", target, label))
        }
        cleanedText = cleanedText.replace(matchResult.value, "")
    }
    
    projectRegex.findAll(rawText).forEach { matchResult ->
        val target = matchResult.groupValues[1]
        val label = when (target.lowercase()) {
            "aion" -> "Try AION router 🕹️"
            "revive" -> "Inspect Revive-OS 🔗"
            "slm" -> "Explore Rajora SLM 🧠"
            "search" -> "Play with Search 🔍"
            else -> "Inspect $target"
        }
        list.add(ParsedRecommendation("PROJECT", target, label))
        cleanedText = cleanedText.replace(matchResult.value, "")
    }
    
    cleanedText = cleanedText.replace(Regex("\\[RECOMMEND_[A-Z]+:[a-zA-Z0-9_-]+\\]"), "")
    
    return Pair(cleanedText.trim(), list)
}
