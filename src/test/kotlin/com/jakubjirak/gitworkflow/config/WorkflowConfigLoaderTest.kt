package com.jakubjirak.gitworkflow.config

import com.jakubjirak.gitworkflow.model.WorkflowPreset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class WorkflowConfigLoaderTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `parse gitflow preset config`() {
        val yaml = """
            preset: gitflow
            branches:
              - pattern: "feature/.+"
                allowed_base: ["develop"]
                description: "Feature branches"
              - pattern: "hotfix/.+"
                allowed_base: ["main"]
                description: "Hotfix branches"
            commits:
              max_subject_length: 80
              conventional_commits: true
              allowed_types: ["feat", "fix", "docs"]
            merge:
              protected_branches: ["main", "develop"]
              require_pr: true
              block_direct_push: true
        """.trimIndent()

        val config = WorkflowConfigLoader.parseConfig(yaml)

        assertEquals(WorkflowPreset.GITFLOW, config.preset)
        assertEquals(2, config.branchRules.size)
        assertEquals("feature/.+", config.branchRules[0].pattern)
        assertEquals(listOf("develop"), config.branchRules[0].allowedBase)
        assertEquals(80, config.commitRules.maxSubjectLength)
        assertTrue(config.commitRules.conventionalCommits)
        assertEquals(listOf("feat", "fix", "docs"), config.commitRules.allowedTypes)
        assertEquals(listOf("main", "develop"), config.mergeRules.protectedBranches)
        assertTrue(config.mergeRules.requirePR)
    }

    @Test
    fun `parse trunk-based preset config`() {
        val yaml = """
            preset: trunk_based
        """.trimIndent()

        val config = WorkflowConfigLoader.parseConfig(yaml)
        assertEquals(WorkflowPreset.TRUNK_BASED, config.preset)
        assertTrue(config.branchRules.isNotEmpty())
    }

    @Test
    fun `parse config with missing fields uses defaults`() {
        val yaml = """
            preset: gitflow
        """.trimIndent()

        val config = WorkflowConfigLoader.parseConfig(yaml)
        assertEquals(WorkflowPreset.GITFLOW, config.preset)
        assertTrue(config.branchRules.isNotEmpty(), "Default branch rules should be loaded")
        assertTrue(config.mergeRules.protectedBranches.isNotEmpty(), "Default merge rules should be loaded")
    }

    @Test
    fun `parse config with unknown preset falls back to custom`() {
        val yaml = """
            preset: unknown_preset
        """.trimIndent()

        val config = WorkflowConfigLoader.parseConfig(yaml)
        assertEquals(WorkflowPreset.CUSTOM, config.preset)
    }

    @Test
    fun `load config from file`() {
        val configFile = tempDir.resolve(".git-workflow.yaml").apply {
            writeText("""
                preset: github_flow
                commits:
                  max_subject_length: 50
                  conventional_commits: false
            """.trimIndent())
        }

        val config = WorkflowConfigLoader.loadFromFile(configFile)

        assertNotNull(config)
        assertEquals(WorkflowPreset.GITHUB_FLOW, config!!.preset)
        assertEquals(50, config.commitRules.maxSubjectLength)
    }

    @Test
    fun `load from directory finds config file`() {
        tempDir.resolve(".git-workflow.yaml").apply {
            writeText("preset: gitflow")
        }

        val config = WorkflowConfigLoader.loadFromDirectory(tempDir)
        assertNotNull(config)
        assertEquals(WorkflowPreset.GITFLOW, config!!.preset)
    }

    @Test
    fun `load from directory returns null when no config`() {
        val config = WorkflowConfigLoader.loadFromDirectory(tempDir)
        assertNull(config)
    }

    @Test
    fun `load non-existent file returns null`() {
        val config = WorkflowConfigLoader.loadFromFile(File("/nonexistent/.git-workflow.yaml"))
        assertNull(config)
    }

    @Test
    fun `parse malformed yaml returns default config`() {
        val config = WorkflowConfigLoader.parseConfig("not: valid: yaml: {{{{")
        assertNotNull(config)
    }

    @Test
    fun `parse empty yaml returns default config`() {
        val config = WorkflowConfigLoader.parseConfig("")
        assertNotNull(config)
    }

    @Test
    fun `default gitflow config has expected branch rules`() {
        val config = WorkflowConfigLoader.getDefaultConfig(WorkflowPreset.GITFLOW)
        val patterns = config.branchRules.map { it.pattern }
        assertTrue("feature/.+" in patterns)
        assertTrue("bugfix/.+" in patterns)
        assertTrue("release/.+" in patterns)
        assertTrue("hotfix/.+" in patterns)
        assertTrue("main" in patterns)
        assertTrue("develop" in patterns)
    }

    @Test
    fun `default trunk-based config has expected branch rules`() {
        val config = WorkflowConfigLoader.getDefaultConfig(WorkflowPreset.TRUNK_BASED)
        assertTrue(config.branchRules.isNotEmpty())
        assertTrue(config.mergeRules.protectedBranches.contains("main"))
    }

    @Test
    fun `commit rules with issue reference parsing`() {
        val yaml = """
            preset: custom
            commits:
              require_issue_ref: true
              issue_ref_pattern: "PROJ-\\d+"
              conventional_commits: false
        """.trimIndent()

        val config = WorkflowConfigLoader.parseConfig(yaml)
        assertTrue(config.commitRules.requireIssueRef)
        assertEquals("PROJ-\\d+", config.commitRules.issueRefPattern)
    }
}
