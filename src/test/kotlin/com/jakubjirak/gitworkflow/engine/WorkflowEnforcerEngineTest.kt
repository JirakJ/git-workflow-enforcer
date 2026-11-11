package com.jakubjirak.gitworkflow.engine

import com.jakubjirak.gitworkflow.config.WorkflowConfigLoader
import com.jakubjirak.gitworkflow.model.ViolationType
import com.jakubjirak.gitworkflow.model.WorkflowPreset
import org.junit.jupiter.api.Assertions.assertEquals
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
    fun `validateAll combines branch commit and merge checks`() {
        val all = WorkflowEnforcerEngine.validateAll("main", "did stuff", gitflow)
        assertTrue(all.any { it.type == ViolationType.COMMIT_FORMAT })
        assertTrue(all.any { it.type == ViolationType.MERGE_POLICY })
        assertTrue(WorkflowEnforcerEngine.validateAll("feature/x", null, gitflow).isEmpty())
    }

    @Test
    fun `invalid user regex does not crash the engine`() {
        val broken = gitflow.copy(commitRules = gitflow.commitRules.copy(requireIssueRef = true, issueRefPattern = "["))
        WorkflowEnforcerEngine.validateCommitMessage("feat: x", broken)
    }
}
