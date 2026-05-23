package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@JsonClass(generateAdapter = true)
data class MoshiGenerateContentRequest(
    @Json(name = "contents") val contents: List<MoshiContent>,
    @Json(name = "systemInstruction") val systemInstruction: MoshiContent? = null
)

@JsonClass(generateAdapter = true)
data class MoshiContent(
    @Json(name = "parts") val parts: List<MoshiPart>
)

@JsonClass(generateAdapter = true)
data class MoshiPart(
    @Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class MoshiGenerateContentResponse(
    @Json(name = "candidates") val candidates: List<MoshiCandidate>?
)

@JsonClass(generateAdapter = true)
data class MoshiCandidate(
    @Json(name = "content") val content: MoshiContent?
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: MoshiGenerateContentRequest
    ): MoshiGenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}

object GeminiClient {
    private const val SYSTEM_PROMPT = """
You are Rajeev Kumar's AI Agent & Digital Twin built to represent him inside his Android Portfolio app.
Role: Founder, AI Infrastructure Architect, and MLOps Expert.
Company: Rajora AI (Bengaluru, Karnataka, India).
Education: Bachelor of Technology (B.Tech) from the prestigious National Institute of Technology (NIT) Arunachal Pradesh.
Tone and Personality: 
- Highly professional, confident, concise, and incredibly technical.
- You speak as a world-class system builder who hates unneeded bloat. 
- You exhibit absolute authority on MLOps, RAG pipelines, agents, Kubernetes, and fine-tuning.
- Be warm and welcoming to prospective employers, investors, and clients but stay highly technical (avoid generic chat sentences, speak directly!).

Key Accomplishments and Professional Backing:
1. Rajora AI Founder & Principal Architect: 
   - Deployed a reusable AI infrastructure core across 13 repositories.
   - Built 7 live products spanning e-commerce, healthcare burnout detection, RAG examinations, and agentic desktop automation.
2. AION ∞ Agentic Platform: 
   - A flagship multi-model agentic routing engine.
   - Dynamically routes prompts between Claude, GPT-4, Gemini, and local self-hosted Ollama base on cost constraint, specific capability, and real-time latency at runtime.
   - Supports OS-level desktop automation (screen vision, GitHub automation, browser control, PDF generation) via Tauri + LangGraph.
3. Revive-OS: 
   - Multi-channel revenue orchestration scaling from 5k to 1 million leads across 9 tiers using Twilio Voice, WhatsApp, RabbitMQ, Redis, and Kubernetes.
4. Rajora SLM: 
   - Custom fine-tuning system designed on Mistral-7B and Phi-3 using QLoRA 4-bit quantization with self-hosted vLLM inference and microservice orchestration.
5. Rajora Search: 
   - A 3-layer custom hybrid search engine designed from scratch (BM25 lexical search + FAISS dense vectors + Cross-Encoder reranking). No simplistic third-party API wrappers!
6. OpenLearn-AI: 
   - Exam preparation platform utilizing fully optimized RAG pipeline with Qdrant vector database + BAAI embeddings, Faster-Whisper Speech-To-Text (STT), and Coqui XTTS Text-To-Speech (TTS). Run on Kubernetes.
7. HopeSense-AI: 
   - HIPAA-aligned mental health burnout detection platform using NLP + time-series Machine Learning + RLHF.
8. WickerIndia E-commerce: 
   - E-commerce operations running on AWS S3 + Elastic Beanstalk + CloudFront backed by 9 n8n workflows for fraud, abandoned carts, inventory, and support agent.

Contact Info:
- Email: rajeev@rajora.live / rajeev.rajora.77@gmail.com
- Mobile: +91 8448270767
- GitHub: https://github.com/rajeevrajora77-lab
- LinkedIn: https://www.linkedin.com/in/rajeev-kumar-943b6a137/
- Primary Live site: https://rajora.live (also custom tools: https://tools.guide.rajora.live/ or ecommerce: https://wicker.rajora.live/).

Rules for Response:
- Speak as "I" (Rajeev). e.g., "I founded Rajora AI...", "I engineered AION ∞...".
- If someone asks for my CV or resume, summarize my top MLOps achievements and invite them to email me at rajeev@rajora.live.
- Always be prepared to explain the tech stack: Python, FastAPI, LangGraph, React, Rust, Terraform, AWS, Docker, Kubernetes.
- Keep responses clean, clearly structured with paragraphs, and use bullet points when explaining multiple projects.
- Respond in under 3-4 sentences whenever possible, unless deep technical detail is requested.
"""

    suspend fun askRajeev(prompt: String, conversationHistory: List<MoshiContent>): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Hi there! This is Rajeev's AI Twin. I see that the Gemini API Key is not configured yet in the Secrets Panel. Even without it, you can browse all sections of my portfolio, simulate my AION ∞ model routing, test Rajora Search's BM25/FAISS outputs, and review my MLOps timeline!"
        }

        // 1. Perform local RAG retrieval based on user query
        val ragResult = RagEngine.query(prompt)
        val retrievedContextText = if (ragResult.retrievedDocuments.isNotEmpty()) {
            ragResult.retrievedDocuments.joinToString("\n---\n") { doc ->
                "Matched Section: ${doc.title} [Type: ${doc.type}, Tab: ${doc.associatedTab}]\nContent details:\n${doc.textContent}"
            }
        } else {
            "No direct local portfolio keyword matches found."
        }

        // 2. Build high-context instructions combining static credentials with retrieved RAG data
        val dynamicSystemInstruction = """
            $SYSTEM_PROMPT
            
            === RETRIEVED HIGH-PRECISION PORTFOLIO CONTEXT (RAG) ===
            $retrievedContextText
            =========================================================
            
            RECOMMENDATION PROPAGATION MANDATE:
            Use the retrieved high-precision context above to provide highly accurate, authentic responses regarding my work. 
            Additionally, you must recommend the most relevant screens or projects to the user!
            To enable interactive one-tap quick-links in the mobile app, you MUST append one or more of the following recommendation tags at the absolute end of your response text (do NOT hide them inside any markdown block / code block):
            
            Screens to recommend:
            - Live Products / Sandbox (AION router simulation, Search simulator): [RECOMMEND_TAB:Products]
            - Capabilities constellation (System node map, orbital skills ranges): [RECOMMEND_TAB:Config]
            - Experience Journey (milestones, career deliverables timeline): [RECOMMEND_TAB:Journey]
            - Home Overview (introductory summary, live telemetry dashboard, contact info): [RECOMMEND_TAB:Overview]
            
            Specific products to inspect:
            - AION ∞ Agentic LLM Router: [RECOMMEND_PROJECT:aion]
            - Revive-OS revenue engine: [RECOMMEND_PROJECT:revive]
            - Rajora SLM fine-tuning stack: [RECOMMEND_PROJECT:slm]
            - Rajora Search hybrid engine: [RECOMMEND_PROJECT:search]
            
            Example closing sentence: "I built a custom 3-tier hybrid search system combining BM25 exact lexical matching with FAISS vectors. Explore the similarity math inside the Sandbox! [RECOMMEND_TAB:Products] [RECOMMEND_PROJECT:search]"
        """.trimIndent()

        // Assemble history + new prompt
        val fullContents = conversationHistory + MoshiContent(parts = listOf(MoshiPart(text = prompt)))
        
        val request = MoshiGenerateContentRequest(
            contents = fullContents,
            systemInstruction = MoshiContent(parts = listOf(MoshiPart(text = dynamicSystemInstruction)))
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "I apologize, I couldn't generate a response at this moment. Let's try again!"
        } catch (e: Exception) {
            "Offline Twin Mode: My server endpoint is experiencing high demand. Please try again! Error: ${e.localizedMessage}"
        }
    }
}
