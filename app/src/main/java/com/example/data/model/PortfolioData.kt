package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class ProjectItem(
    val id: String,
    val title: String,
    val tag: String,
    val description: String,
    val metrics: String,
    val tech: List<String>,
    val highlight: String,
    val demoTitle: String
)

data class TimelineItem(
    val period: String,
    val role: String,
    val company: String,
    val location: String,
    val type: String,
    val bulletPoints: List<String>,
    val isHighlight: Boolean = false
)

data class SkillItem(
    val name: String,
    val level: Float, // 0.0f to 1.0f
    val iconName: String
)

data class SkillCategory(
    val categoryName: String,
    val iconName: String,
    val skills: List<SkillItem>
)

object PortfolioRepository {
    val projects = listOf(
        ProjectItem(
            id = "aion",
            title = "AION ∞",
            tag = "Agentic LLM Router",
            description = "A flagship agentic routing engine that evaluates requested prompts and dynamically routes them across Claude, GPT-4, Gemini, and safe self-hosted local Ollama servers to optimize cost, latency, and capability constraints in real-time.",
            metrics = "Sub-100ms routing routing delay, 72% average cost reduction, full OS integration.",
            tech = listOf("LangGraph", "FastAPI", "Python", "Tauri", "Rust", "vLLM", "Ollama", "Docker"),
            highlight = "Engineered multi-model fallback cascades and OS-level client orchestrators giving agents screen vision and GitHub commit capabilities.",
            demoTitle = "AION Routing Emulator"
        ),
        ProjectItem(
            id = "revive",
            title = "Revive-OS",
            tag = "Revenue Orchestration",
            description = "Enterprise-scale multi-channel AI campaign and lead conversion framework automating outbound voice calls and WhatsApp funnels from 5,000 structure sizes up to 1M leads.",
            metrics = "99.95% engine availability, 4.2M database events, 42% spike in lead callback engagement.",
            tech = listOf("Python", "RabbitMQ", "Kubernetes", "Redis", "Twilio Voice", "WhatsApp-REST", "FastAPI"),
            highlight = "Implemented HIPAA-compliant records containment and asynchronous event prioritization utilizing customized RabbitMQ queueing.",
            demoTitle = "Twilio/RabbitMQ Event Pipeline Map"
        ),
        ProjectItem(
            id = "slm",
            title = "Rajora SLM",
            tag = "Fine-Tuning Stack & vLLM",
            description = "Industrial-grade fine-tuning environment for Mistral-7B and Phi-3. Utilizes QLoRA 4-bit precision quantization, custom model checkpoints, and Triton Inference Server optimization.",
            metrics = "68% lower memory footprint, high token production speeds under heavy multi-tenant scale.",
            tech = listOf("PyTorch", "HuggingFace", "QLoRA", "vLLM", "Docker", "Triton Server", "AWS EC2 g5"),
            highlight = "Designed an 8-component microservice stack managing distributed model registries, feature stores, and continuous model health observability.",
            demoTitle = "QLoRA Model Stats & Quantization Graph"
        ),
        ProjectItem(
            id = "search",
            title = "Rajora Search",
            tag = "Custom Hybrid Search",
            description = "A custom-engineered 3-tier hybrid search stack combining BM25 exact lexical matches, dense sentence-transformers vector embeddings via FAISS index, and cross-encoder neural model rerankings.",
            metrics = "+23% NDCG@10 lookup precision, sub-12ms response delivery on highly sparse target datasets.",
            tech = listOf("SentenceTransformers", "FAISS", "BM25", "NumPy", "Python", "FastAPI", "PostgreSQL"),
            highlight = "Designed entirely from scratch as pre-compiled native arrays rather than invoking brittle or bloated cloud wrappers, achieving true bare-metal performance.",
            demoTitle = "Active Search Similarity playground"
        )
    )

    val timeline = listOf(
        TimelineItem(
            period = "2023 - Present",
            role = "Founder & Principal Architect",
            company = "Rajora AI",
            location = "Bengaluru, Karnataka (Full-time)",
            type = "Self-employed",
            bulletPoints = listOf(
                "Orchestrated reusable AI foundational blueprint deployed in production across 13 repositories, speeding up product launches by 4x.",
                "Engineered AION ∞ with LangGraph 0.2.60 routing across leading proprietary and open source models saving hundreds of thousands of dollars.",
                "Built and deployed high-converting Revive-OS lead systems utilizing high-throughput container clusters on AWS Kubernetes (EKS).",
                "Successfully architected HIPAA and COPPA compliant data vaults with enterprise security safeguards (AES-256-GCM, RS256 JWT, continuous safety audits)."
            ),
            isHighlight = true
        ),
        TimelineItem(
            period = "2022 - Present",
            role = "AI/ML MLOps Senior Consultant",
            company = "Independent Consultant / Freelance",
            location = "Remote / India",
            type = "Freelance",
            bulletPoints = listOf(
                "Designed end-to-end MLOps automation pipelines managing raw data ingestion, drift monitoring, validation, and container deployments.",
                "Spun up ultra-scalable Dockerized AI microservices on AWS achieving seamless support for 1,000+ concurrent requests/second.",
                "Integrated Prometheus and Grafana for centralized logging and continuous training feedback loops, reducing model deployment cycles by 3x.",
                "Consulted with SaaS enterprises on tuning inference models for latency optimization, keeping 95th-percentile response under 250ms."
            )
        ),
        TimelineItem(
            period = "2019 - 2022",
            role = "B.Tech in Technology",
            company = "National Institute of Technology (NIT) Arunachal Pradesh",
            location = "Arunachal Pradesh, India",
            type = "Education",
            bulletPoints = listOf(
                "Studied Computer Science & Systems, focusing on Machine Learning algorithms, Advanced Databases, and distributed systems logic.",
                "Led collegiate developer clubs and built prototype neural networks, participating in regional tech challenge finals.",
                "Laid strong academic grounds in linear algebra, time-series forecasting models, and cloud infrastructure paradigms."
            )
        )
    )

    val skillCategories = listOf(
        SkillCategory(
            categoryName = "Agentic & RAG Orchestration",
            iconName = "SmartToy",
            skills = listOf(
                SkillItem("LangGraph / Multi-Agents", 0.95f, "AccountTree"),
                SkillItem("LangChain Framework", 0.90f, "Layers"),
                SkillItem("RAG & Hybrid Search", 0.93f, "Search"),
                SkillItem("BM25 / FAISS Retrieval", 0.92f, "BubbleChart"),
                SkillItem("Semantic Embedding Models", 0.88f, "GraphicEq")
            )
        ),
        SkillCategory(
            categoryName = "MLOps & Fine-Tuning",
            iconName = "Memory",
            skills = listOf(
                SkillItem("Model Quantization (QLoRA)", 0.92f, "CompassCalibration"),
                SkillItem("vLLM / Triton Deployment", 0.91f, "Speed"),
                SkillItem("PyTorch & Transformers", 0.87f, "Science"),
                SkillItem("MLflow Tracking Core", 0.89f, "Timeline"),
                SkillItem("Prefect & Airflow Schedulers", 0.85f, "Schedule")
            )
        ),
        SkillCategory(
            categoryName = "Cloud, DevOps & Backend",
            iconName = "Cloud",
            skills = listOf(
                SkillItem("Kubernetes / Docker Scaling", 0.94f, "ViewQuilt"),
                SkillItem("AWS Elastic Beanstalk / SQS", 0.90f, "CloudQueue"),
                SkillItem("FastAPI / Python (Async)", 0.95f, "FlashOn"),
                SkillItem("Terraform IaC Assets", 0.86f, "Language"),
                SkillItem("GitHub Actions CI/CD Build", 0.91f, "FactCheck")
            )
        ),
        SkillCategory(
            categoryName = "Databases & Observability",
            iconName = "Storage",
            skills = listOf(
                SkillItem("PostgreSQL & Advanced SQL", 0.88f, "Storage"),
                SkillItem("Redis Caching Structures", 0.92f, "SdStorage"),
                SkillItem("Monitored Slack / Grafana", 0.90f, "Monitoring"),
                SkillItem("RabbitMQ Queuing Systems", 0.89f, "SettingsEthernet")
            )
        )
    )
}
