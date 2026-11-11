package com.jakubjirak.gitworkflow.engine

import com.jakubjirak.gitworkflow.config.WorkflowConfigLoader
import com.jakubjirak.gitworkflow.model.Severity
import com.jakubjirak.gitworkflow.model.ViolationType
import com.jakubjirak.gitworkflow.model.WorkflowPreset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WorkflowEnforcerEngineTest {

    private val gitflow = WorkflowConfigLoader.getDefaultConfig(WorkflowPreset.GITFLOW)

    @Test
    fun `compliant branch and commit produce no violations`() {
        assertTrue(WorkflowEnforcerEngine.validateBranch("feature/login", gitflow).isEmpty())
        assertTrue(WorkflowEnforcerEngine.validateCommitMessage("feat(auth): add login", gitflow).isEmpty())
    }

    @Test
    fun `bad branch and commit are reported with their types`() {
        assertEquals(ViolationType.BRANCH_NAME, WorkflowEnforcerEngine.validateBranch("wip", gitflow).single().type)
        val commit = WorkflowEnforcerEngine.validateCommitMessage("did stuff", gitflow)
        assertTrue(commit.isNotEmpty())
        assertTrue(commit.all { it.type == ViolationType.COMMIT_FORMAT })
    }

    @Test
    fun `validateCommit combines message, branch name and protected branch checks`() {
        val all = WorkflowEnforcerEngine.validateCommit("did stuff", "main", gitflow)
        assertTrue(all.any { it.type == ViolationType.COMMIT_FORMAT })
        assertTrue(all.any { it.type == ViolationType.PROTECTED_BRANCH && it.severity == Severity.ERROR })
        assertTrue(WorkflowEnforcerEngine.validateCommit("feat: login", "feature/x", gitflow).isEmpty())
    }

    @Test
    fun `gitflow preset protects develop too`() {
        val violations = WorkflowEnforcerEngine.validateCommit("feat: x", "develop", gitflow)
        assertEquals(ViolationType.PROTECTED_BRANCH, violations.single().type)
    }

    @Test
    fun `protected branch check runs even with message and branch-name validation switched off`() {
        val violations = WorkflowEnforcerEngine.validateCommit(
            "did stuff", "main", gitflow, checkMessage = false, checkBranchName = false,
        )
        assertEquals(ViolationType.PROTECTED_BRANCH, violations.single().type)
    }

    @Test
    fun `message and branch-name toggles switch their checks off`() {
        val noMessage = WorkflowEnforcerEngine.validateCommit("did stuff", "wip", gitflow, checkMessage = false)
        assertFalse(noMessage.any { it.type == ViolationType.COMMIT_FORMAT })
        assertTrue(noMessage.any { it.type == ViolationType.BRANCH_NAME })

        val noBranch = WorkflowEnforcerEngine.validateCommit("did stuff", "wip", gitflow, checkBranchName = false)
        assertTrue(noBranch.any { it.type == ViolationType.COMMIT_FORMAT })
        assertFalse(noBranch.any { it.type == ViolationType.BRANCH_NAME })
    }

    @Test
    fun `detached HEAD skips branch checks`() {
        val violations = WorkflowEnforcerEngine.validateCommit("feat: x", null, gitflow)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `git-workflow yaml drives the protected branch policy`() {
        val warnOnly = WorkflowConfigLoader.parseConfig(
            """
            preset: github_flow
            merge:
              protected_branches: ["main", "release/.*"]
              require_pr: true
              block_direct_push: false
            """.trimIndent()
        )
        val onRelease = WorkflowEnforcerEngine.validateCommit("x", "release/2.0", warnOnly, checkMessage = false, checkBranchName = false)
        assertEquals(Severity.WARNING, onRelease.single().severity)

        val off = WorkflowConfigLoader.parseConfig(
            """
            preset: github_flow
            merge:
              protected_branches: ["main"]
              require_pr: false
              block_direct_push: false
            """.trimIndent()
        )
        assertTrue(WorkflowEnforcerEngine.validateCommit("x", "main", off, checkMessage = false, checkBranchName = false).isEmpty())
    }

    @Test
    fun `invalid user regex does not crash the engine`() {
        val broken = gitflow.copy(commitRules = gitflow.commitRules.copy(requireIssueRef = true, issueRefPattern = "["))
        WorkflowEnforcerEngine.validateCommitMessage("feat: x", broken)
    }
}
