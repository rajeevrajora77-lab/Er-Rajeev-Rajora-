package com.example.data.api

import com.example.data.model.PortfolioRepository
import com.example.data.model.ProjectItem
import com.example.data.model.TimelineItem
import com.example.PortfolioTab

data class RagDocument(
    val id: String,
    val type: String, // "project", "timeline", "skill"
    val title: String,
    val textContent: String,
    val associatedTab: PortfolioTab,
    val associatedProjectId: String? = null
)

object RagEngine {
    private val stopWords = setOf(
        "the", "a", "and", "or", "of", "to", "in", "is", "for", "with", "on", "at", "by", "an", "this", "that", 
        "from", "it", "are", "as", "be", "was", "were", "has", "have", "had", "been", "will", "would", "should", "can", "about", "your", "my"
    )

    private val documents: List<RagDocument> by lazy {
        val list = mutableListOf<RagDocument>()

        // 1. Projects
        PortfolioRepository.projects.forEach { project ->
            val content = """
                Project: ${project.title}
                Tag: ${project.tag}
                Description: ${project.description}
                Metrics: ${project.metrics}
                Tech Stack: ${project.tech.joinToString(", ")}
                Highlight: ${project.highlight}
                Demo: ${project.demoTitle}
            """.trimIndent()
            list.add(
                RagDocument(
                    id = "project_${project.id}",
                    type = "project",
                    title = project.title,
                    textContent = content,
                    associatedTab = PortfolioTab.Products,
                    associatedProjectId = project.id
                )
            )
        }

        // 2. Timeline Experience
        PortfolioRepository.timeline.forEachIndexed { idx, item ->
            val content = """
                Role: ${item.role}
                Company: ${item.company}
                Location: ${item.location}
                Type: ${item.type}
                Period: ${item.period}
                Deliverables Bullet Points: ${item.bulletPoints.joinToString(" ")}
            """.trimIndent()
            list.add(
                RagDocument(
                    id = "timeline_$idx",
                    type = "timeline",
                    title = "${item.role} at ${item.company}",
                    textContent = content,
                    associatedTab = PortfolioTab.Journey
                )
            )
        }

        // 3. Skills
        PortfolioRepository.skillCategories.forEachIndexed { catIdx, category ->
            category.skills.forEachIndexed { skillIdx, skill ->
                val content = """
                    Skill: ${skill.name}
                    Category: ${category.categoryName}
                    Proficiency Level: ${String.format("%.0f%%", skill.level * 100)}
                """.trimIndent()
                list.add(
                    RagDocument(
                        id = "skill_${catIdx}_$skillIdx",
                        type = "skill",
                        title = skill.name,
                        textContent = content,
                        associatedTab = PortfolioTab.Config
                    )
                )
            }
        }
        
        list
    }

    // Simple TF-IDF term overlap scoring
    fun query(userQuery: String): MatchResult {
        val queryTokens = userQuery.lowercase()
            .replace(Regex("[^a-zA-Z0-9 ]"), " ")
            .split(" ")
            .map { it.trim() }
            .filter { it.length > 1 && !stopWords.contains(it) }
            .toSet()

        if (queryTokens.isEmpty()) {
            return MatchResult(emptyList(), PortfolioTab.Overview, null)
        }

        val scoredDocs = documents.map { doc ->
            val docTokens = doc.textContent.lowercase()
                .replace(Regex("[^a-zA-Z0-9 ]"), " ")
                .split(" ")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
            
            val docWordsSet = docTokens.toSet()

            var score = 0.0
            queryTokens.forEach { token ->
                if (docWordsSet.contains(token)) {
                    val frequency = docTokens.count { it == token }
                    score += 1.0 + (frequency * 0.25)

                    // Title/Topic Match premium boost
                    if (doc.title.lowercase().contains(token)) {
                        score += 3.5
                    }
                }
            }
            doc to score
        }.filter { it.second > 0.0 }
         .sortedByDescending { it.second }

        val retList = scoredDocs.take(3).map { it.first }

        val highestDoc = scoredDocs.firstOrNull()?.first
        
        // Return tab recommendations with fallback logic
        val recommendedTab = highestDoc?.associatedTab ?: PortfolioTab.Overview
        val recommendedProject = highestDoc?.associatedProjectId

        return MatchResult(retList, recommendedTab, recommendedProject)
    }

    data class MatchResult(
        val retrievedDocuments: List<RagDocument>,
        val recommendedTab: PortfolioTab,
        val recommendedProjectId: String?
    )
}
