package com.jakubjirak.gitworkflow.config

import com.jakubjirak.gitworkflow.model.BranchRule
import com.jakubjirak.gitworkflow.model.CommitRule
import com.jakubjirak.gitworkflow.model.MergeRule
import com.jakubjirak.gitworkflow.model.WorkflowConfig
import com.jakubjirak.gitworkflow.model.WorkflowPreset
import com.intellij.openapi.diagnostic.Logger
import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 * Loads workflow configuration from .git-workflow.yaml files.
 * Supports preset-based configuration with custom overrides.
 */
object WorkflowConfigLoader {

    private val LOG = Logger.getInstance(WorkflowConfigLoader::class.java)
    const val CONFIG_FILENAME = ".git-workflow.yaml"

    fun loadFromFile(file: File): WorkflowConfig? {
        if (!file.exists()) return null
        return try {
            parseConfig(file.readText())
        } catch (e: Exception) {
            LOG.warn("Failed to load workflow config from ${file.path}", e)
            null
        }
    }

    fun loadFromDirectory(directory: File): WorkflowConfig? {
        return loadFromFile(directory.resolve(CONFIG_FILENAME))
    }

    @Suppress("UNCHECKED_CAST")
    fun parseConfig(yamlContent: String): WorkflowConfig {
        val yaml = Yaml()
        val data = try {
            yaml.load<Map<String, Any?>>(yamlContent)
        } catch (e: Exception) {
            LOG.warn("Failed to parse YAML config", e)
            null
        } ?: return getDefaultConfig(WorkflowPreset.GITFLOW)

        val presetName = (data["preset"] as? String)?.uppercase() ?: "GITFLOW"
        val preset = try {
            WorkflowPreset.valueOf(presetName)
        } catch (_: IllegalArgumentException) {
            WorkflowPreset.CUSTOM
        }

        val baseConfig = getDefaultConfig(preset)

        val branchRules = parseBranchRules(data["branches"] as? List<Map<String, Any?>>)
        val commitRules = parseCommitRules(data["commits"] as? Map<String, Any?>)
        val mergeRules = parseMergeRules(data["merge"] as? Map<String, Any?>)

        return WorkflowConfig(
            preset = preset,
            branchRules = branchRules.ifEmpty { baseConfig.branchRules },
            commitRules = commitRules ?: baseConfig.commitRules,
            mergeRules = mergeRules ?: baseConfig.mergeRules,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseBranchRules(branches: List<Map<String, Any?>>?): List<BranchRule> {
        if (branches == null) return emptyList()
        return branches.mapNotNull { entry ->
            try {
                val pattern = entry["pattern"] as? String ?: return@mapNotNull null
                val allowedBase = (entry["allowed_base"] as? List<String>) ?: emptyList()
                val description = entry["description"] as? String ?: ""
                BranchRule(pattern = pattern, allowedBase = allowedBase, description = description)
            } catch (e: Exception) {
                LOG.debug("Skipping malformed branch rule: $entry", e)
                null
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseCommitRules(commits: Map<String, Any?>?): CommitRule? {
        if (commits == null) return null
        return try {
            CommitRule(
                formatPattern = commits["format_pattern"] as? String ?: "",
                maxSubjectLength = (commits["max_subject_length"] as? Int) ?: 72,
                requireIssueRef = (commits["require_issue_ref"] as? Boolean) ?: false,
                issueRefPattern = commits["issue_ref_pattern"] as? String ?: "[A-Z]+-\\d+",
                conventionalCommits = (commits["conventional_commits"] as? Boolean) ?: false,
                allowedTypes = (commits["allowed_types"] as? List<String>)
                    ?: listOf("feat", "fix", "docs", "style", "refactor", "perf", "test", "build", "ci", "chore", "revert"),
            )
        } catch (e: Exception) {
            LOG.debug("Failed to parse commit rules", e)
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseMergeRules(merge: Map<String, Any?>?): MergeRule? {
        if (merge == null) return null
        return try {
            MergeRule(
                protectedBranches = (merge["protected_branches"] as? List<String>) ?: listOf("main", "master"),
                requirePR = (merge["require_pr"] as? Boolean) ?: true,
                blockDirectPush = (merge["block_direct_push"] as? Boolean) ?: true,
            )
        } catch (e: Exception) {
            LOG.debug("Failed to parse merge rules", e)
            null
        }
    }

    fun getDefaultConfig(preset: WorkflowPreset): WorkflowConfig {
        return when (preset) {
            WorkflowPreset.GITFLOW -> gitflowConfig()
            WorkflowPreset.TRUNK_BASED -> trunkBasedConfig()
            WorkflowPreset.GITHUB_FLOW -> githubFlowConfig()
            WorkflowPreset.CUSTOM -> WorkflowConfig(preset = WorkflowPreset.CUSTOM)
        }
    }

    private fun gitflowConfig(): WorkflowConfig = WorkflowConfig(
        preset = WorkflowPreset.GITFLOW,
        branchRules = listOf(
            BranchRule("feature/.+", listOf("develop"), "Feature branches from develop"),
            BranchRule("bugfix/.+", listOf("develop"), "Bugfix branches from develop"),
            BranchRule("release/.+", listOf("develop"), "Release branches from develop"),
            BranchRule("hotfix/.+", listOf("main", "master"), "Hotfix branches from main"),
            BranchRule("main", emptyList(), "Main branch"),
            BranchRule("master", emptyList(), "Master branch"),
            BranchRule("develop", emptyList(), "Develop branch"),
        ),
        commitRules = CommitRule(maxSubjectLength = 72, conventionalCommits = true),
        mergeRules = MergeRule(
            protectedBranches = listOf("main", "master", "develop"),
            requirePR = true,
            blockDirectPush = true,
        ),
    )

    private fun trunkBasedConfig(): WorkflowConfig = WorkflowConfig(
        preset = WorkflowPreset.TRUNK_BASED,
        branchRules = listOf(
            BranchRule("[a-z0-9-]+/[a-z0-9-]+", listOf("main"), "Short-lived branches from main"),
            BranchRule("main", emptyList(), "Main branch"),
        ),
        commitRules = CommitRule(maxSubjectLength = 72, conventionalCommits = false),
        mergeRules = MergeRule(
            protectedBranches = listOf("main"),
            requirePR = true,
            blockDirectPush = true,
        ),
    )

    private fun githubFlowConfig(): WorkflowConfig = WorkflowConfig(
        preset = WorkflowPreset.GITHUB_FLOW,
        branchRules = listOf(
            BranchRule("[a-z0-9]+(-[a-z0-9]+)*", listOf("main"), "Feature branches from main"),
            BranchRule("main", emptyList(), "Main branch"),
        ),
        commitRules = CommitRule(maxSubjectLength = 72, conventionalCommits = false),
        mergeRules = MergeRule(
            protectedBranches = listOf("main"),
            requirePR = true,
            blockDirectPush = true,
        ),
    )
}
