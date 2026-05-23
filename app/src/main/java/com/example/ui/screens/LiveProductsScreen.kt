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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PortfolioRepository
import com.example.data.model.ProjectItem
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LiveProductsScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Sandbox 1: AION Router simulation states
    var routerQuery by remember { mutableStateOf("") }
    var routerLog by remember { mutableStateOf<List<String>>(emptyList()) }
    var routerDecisionModel by remember { mutableStateOf("") }
    var routerCostSavings by remember { mutableStateOf("") }
    var routerLatencyResult by remember { mutableStateOf("") }
    var isRoutingActive by remember { mutableStateOf(false) }

    // Sandbox 2: Rajora Search simulation states
    var searchQuery by remember { mutableStateOf("") }
    var searchFinished by remember { mutableStateOf(false) }
    var bm25Ratio by remember { mutableStateOf(0.0f) }
    var faissRatio by remember { mutableStateOf(0.0f) }
    var rerankerScore by remember { mutableStateOf(0.0f) }
    var matchedDocuments by remember { mutableStateOf<List<SearchDoc>>(emptyList()) }

    val routerOptions = listOf(
        "Summarize 300 page banking dossier",
        "Generate automated tests for Rust web server",
        "Perform live browser actions to checkout flat widgets",
        "Translate standard hello world text into Spanish"
    )

    val searchOptions = listOf(
        "HIPAA cloud architecture AWS",
        "FastAPI latency optimizer",
        "Kubernetes multi tenant scheduler",
        "LangGraph parallel agent patterns"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Core Header
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Live Launch Suite",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Browse Rajora AI's live products and run live local emulation logic on core services.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // --- SECTION A: PRODUCT CARDS REVEALS ---
        PortfolioRepository.projects.forEach { project ->
            ProductLaunchCard(project = project)
        }

        Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f), thickness = 1.dp)

        // --- SECTION B: INTERACTIVE PLAYROOM 1: AION ROUTER SIMULATION ---
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AltRoute,
                        contentDescription = "AION ROUTER",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "AION ∞ Interactive Playroom",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "AION routes user queries dynamically between major industry models and local models to optimize budget and processing performance.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Quick Presets
                Text(
                    text = "Tap a preset query to simulate:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    routerOptions.forEach { option ->
                        Text(
                            text = option,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    routerQuery = option
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                // TextInput
                OutlinedTextField(
                    value = routerQuery,
                    onValueChange = { routerQuery = it },
                    label = { Text("Enter prompt class or instructions") },
                    placeholder = { Text("Write query or custom problem...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("router_emulator_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (routerQuery.isNotBlank() && !isRoutingActive) {
                            scope.launch {
                                isRoutingActive = true
                                routerLog = listOf("AION.core initialized. Receiving input pipeline...")
                                delay(700)
                                routerLog = routerLog + listOf("Analyzing token dimensions... length: ${routerQuery.length * 4} context units.")
                                delay(600)
                                val hasAction = routerQuery.contains("action", ignoreCase = true) || routerQuery.contains("browser", ignoreCase = true)
                                val isHeavy = routerQuery.contains("page", ignoreCase = true) || routerQuery.contains("dossier", ignoreCase = true) || routerQuery.contains("legal", ignoreCase = true)
                                val isSimple = routerQuery.length < 25
                                
                                routerLog = routerLog + listOf("Reviewing model tier criteria constraints...")
                                delay(700)

                                if (hasAction) {
                                    routerDecisionModel = "Ollama Local (Mistral-7B via Tauri Host)"
                                    routerCostSavings = "99.4% (Zero SaaS API usage charges)"
                                    routerLatencyResult = "42 ms (Hardware direct network)"
                                    routerLog = routerLog + listOf(
                                        "Evaluating security permissions: Internal desktop automation approved.",
                                        "Routing path: Local host executor node chosen."
                                    )
                                } else if (isHeavy) {
                                    routerDecisionModel = "Claude-3.5-Sonnet Cloud Enterprise"
                                    routerCostSavings = "24.0% (Context cached compression)"
                                    routerLatencyResult = "1240 ms (Heavy reasoning node)"
                                    routerLog = routerLog + listOf(
                                        "Prompt requires extensive context reasoning.",
                                        "Routing path: Proprietary Cloud Tier 1."
                                    )
                                } else if (isSimple) {
                                    routerDecisionModel = "Ollama Local (Phi-3 Mini)"
                                    routerCostSavings = "100% (No network toll)"
                                    routerLatencyResult = "18 ms (Edge quantization runtime)"
                                    routerLog = routerLog + listOf(
                                        "Small semantic load verified. Local quantization sufficient.",
                                        "Routing path: Edge offline node."
                                    )
                                } else {
                                    routerDecisionModel = "GPT-4o API Cascade Node"
                                    routerCostSavings = "62.5% (AION routing fallback cache hit)"
                                    routerLatencyResult = "390 ms (Cached edge proxy CDN)"
                                    routerLog = routerLog + listOf(
                                        "General task. Serving from regional prompt cache indices.",
                                        "Routing path: Multi-Tenant CDN Router."
                                    )
                                }
                                delay(400)
                                routerLog = routerLog + listOf("Stream pipe completed. Dispatch success.")
                                isRoutingActive = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("router_emulator_run_button"),
                    shape = RoundedCornerShape(12.dp),
                    enabled = routerQuery.isNotBlank() && !isRoutingActive
                ) {
                    if (isRoutingActive) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.background)
                    } else {
                        Text("Simulate AION ∞ Router Routing", fontWeight = FontWeight.Bold)
                    }
                }

                // Log Window Output
                AnimatedVisibility(visible = routerLog.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "AION SYSTEM TERMINAL OUTPUT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        routerLog.forEach { log ->
                            Text(
                                text = "➜ $log",
                                fontSize = 11.sp,
                                color = Color.LightGray,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }

                        if (!isRoutingActive && routerDecisionModel.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text("AION ESTIMATED SYSTEM SOLUTION:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981), fontFamily = FontFamily.Monospace)
                            
                            KeyValueTerminalLine("Assigned Provider", routerDecisionModel)
                            KeyValueTerminalLine("Budget Retained", routerCostSavings)
                            KeyValueTerminalLine("Network Latency", routerLatencyResult)
                        }
                    }
                }
            }
        }

        // --- SECTION C: INTERACTIVE PLAYROOM 2: RAJORA SEARCH SIMULATION ---
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.YoutubeSearchedFor,
                        contentDescription = "RAJORA SEARCH",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Rajora Search 3-Layer Simulator",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "A dense custom Search simulator that coordinates lexical exact-text scoring (BM25) with high-density vector alignment (FAISS indexes) and neural reranking scales.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Quick Presets
                Text(
                    text = "Tap a preset system term:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    searchOptions.forEach { option ->
                        Text(
                            text = option,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    searchQuery = option
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                // TextInput
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Query search indices") },
                    placeholder = { Text("Perform vector lookup...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_emulator_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (searchQuery.isNotBlank()) {
                            scope.launch {
                                searchFinished = false
                                delay(600)
                                // Generate mock dynamic vector indices and lexical match values
                                val lower = searchQuery.lowercase()
                                val isAws = lower.contains("aws") || lower.contains("hipaa") || lower.contains("cloud")
                                val isFast = lower.contains("fast") || lower.contains("latency") || lower.contains("optimizer")
                                val isK8s = lower.contains("k8s") || lower.contains("kubernetes") || lower.contains("tenant")
                                val isLang = lower.contains("lang") || lower.contains("agent") || lower.contains("chain")

                                if (isAws) {
                                    bm25Ratio = 0.82f
                                    faissRatio = 0.79f
                                    rerankerScore = 0.94f
                                    matchedDocuments = listOf(
                                        SearchDoc("AWS Cloud HIPAA Data Vault", "Encrypts ePHI data with AES-256-GCM. Uses tight IAM definitions, cloudfront, S3 isolated storage, and automated log audits.", "+23% efficiency"),
                                        SearchDoc("Revive-OS Infrastructure blueprint", "Scaled out AWS EKS containers across multi-tier queues on RabbitMQ. Fully isolated HIPAA-ready cloud setup.", "+41% deployment cost")
                                    )
                                } else if (isFast) {
                                    bm25Ratio = 0.94f
                                    faissRatio = 0.73f
                                    rerankerScore = 0.96f
                                    matchedDocuments = listOf(
                                        SearchDoc("FastAPI latency metrics and tuning hooks", "Slashes endpoint delay into sub-250ms range through optimized async event loops and uvicorn worker threads.", "-45% response latency"),
                                        SearchDoc("vLLM Triton Multi-Tenant registry", "Configured high speed model prediction caches. Sub-50ms token emission speed.", "+15% token speeds")
                                    )
                                } else if (isK8s) {
                                    bm25Ratio = 0.71f
                                    faissRatio = 0.88f
                                    rerankerScore = 0.89f
                                    matchedDocuments = listOf(
                                        SearchDoc("High Scale K8s Pod scheduler", "Kubernetes cluster orchestration powering 2M active lead conversions, supporting multi-tenancy and resource pools.", "99.9% uptime validation"),
                                        SearchDoc("OpenLearn-AI Exam Kubernetes micro-stack", "Deploys RAG vector stores on isolated node sets to prevent CPU resource limits from choking audio engines.", "Zero network starvation")
                                    )
                                } else if (isLang) {
                                    bm25Ratio = 0.90f
                                    faissRatio = 0.84f
                                    rerankerScore = 0.95f
                                    matchedDocuments = listOf(
                                        SearchDoc("LangGraph 0.2.60 Advanced Multi-Agent routing", "Manages state cycles and fallback channels across OpenAI, Antropic, and safe local Llama model cascades.", "Sub-100ms evaluation"),
                                        SearchDoc("AION ∞ screen vision desktop browser actions", "Utilizes LangGraph workflows to interpret pixel arrays and commit clean git changes safely.", "Autonomous git completions")
                                    )
                                } else {
                                    // General results
                                    bm25Ratio = 0.61f + (Random.nextDouble() * 0.2).toFloat()
                                    faissRatio = 0.65f + (Random.nextDouble() * 0.2).toFloat()
                                    rerankerScore = 0.75f + (Random.nextDouble() * 0.2).toFloat()
                                    matchedDocuments = listOf(
                                        SearchDoc("Rajora Core Systems Overview", "13 active repositories sharing unified MLOps plumbing code across multi-tenant domains.", "4x rollout speed improvement")
                                    )
                                }
                                searchFinished = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("search_emulator_run_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    enabled = searchQuery.isNotBlank()
                ) {
                    Text("Compute Hybrid 3-Layer Search Alignments", fontWeight = FontWeight.Bold)
                }

                // Search Score Details Display
                AnimatedVisibility(visible = searchFinished) {
                    Column(modifier = Modifier.padding(top = 18.dp)) {
                        Text(
                            text = "COMPUTED EMBEDDING & TEXT SCORE MATCHES:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ScoreProgressBarCard(title = "Lexical (BM25)", ratio = bm25Ratio, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                            ScoreProgressBarCard(title = "Vector (FAISS)", ratio = faissRatio, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
                            ScoreProgressBarCard(title = "Cross-Rerank", ratio = rerankerScore, color = Color(0xFF10B981), modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "RERANKED CORPUS DOCUMENTS MATCHING IN INDEX:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        matchedDocuments.forEach { doc ->
                            DocResultRow(doc = doc)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductLaunchCard(
    project: ProjectItem
) {
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
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.tag.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "LIVE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = project.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = project.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Highlight Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f), shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Highlight",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Production Deliverable Highlight",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = project.highlight,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "METRICS & RESULTS:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )

            Text(
                text = project.metrics,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10B981)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tech Row list
            Text(
                text = "ENGINEERING STACK:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                project.tech.take(5).forEach { techItem ->
                    Text(
                        text = techItem,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun KeyValueTerminalLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontSize = 11.sp,
            color = Color.Gray,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = value,
            fontSize = 11.sp,
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ScoreProgressBarCard(
    title: String,
    ratio: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f), shape = RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
            Text(String.format("%.1f%%", ratio * 100), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(ratio)
                        .background(color)
                )
            }
        }
    }
}

data class SearchDoc(
    val title: String,
    val text: String,
    val secondaryValue: String
)

@Composable
fun DocResultRow(doc: SearchDoc) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = doc.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = doc.secondaryValue,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10B981)
            )
        }
        Text(
            text = doc.text,
            fontSize = 11.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
