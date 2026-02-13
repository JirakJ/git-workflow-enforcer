package com.jakubjirak.gitworkflow.validators

import com.jakubjirak.gitworkflow.config.WorkflowConfigLoader
import com.jakubjirak.gitworkflow.model.BranchRule
import com.jakubjirak.gitworkflow.model.ViolationType
import com.jakubjirak.gitworkflow.model.WorkflowConfig
import com.jakubjirak.gitworkflow.model.WorkflowPreset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BranchNameValidatorTest {

    private val gitflowConfig = WorkflowConfigLoader.getDefaultConfig(WorkflowPreset.GITFLOW)

    @Test
    fun `valid feature branch passes gitflow validation`() {
        val violations = BranchNameValidator.validate("feature/add-login", gitflowConfig)
        assertTrue(violations.isEmpty(), "Expected no violations for valid feature branch")
    }

    @Test
    fun `valid bugfix branch passes gitflow validation`() {
        val violations = BranchNameValidator.validate("bugfix/fix-header", gitflowConfig)
        assertTrue(violations.isEmpty(), "Expected no violations for valid bugfix branch")
    }

    @Test
    fun `valid release branch passes gitflow validation`() {
        val violations = BranchNameValidator.validate("release/1.0.0", gitflowConfig)
        assertTrue(violations.isEmpty(), "Expected no violations for valid release branch")
    }

    @Test
    fun `valid hotfix branch passes gitflow validation`() {
        val violations = BranchNameValidator.validate("hotfix/critical-fix", gitflowConfig)
        assertTrue(violations.isEmpty(), "Expected no violations for valid hotfix branch")
    }

    @Test
    fun `main branch passes gitflow validation`() {
        val violations = BranchNameValidator.validate("main", gitflowConfig)
        assertTrue(violations.isEmpty(), "Expected no violations for main branch")
    }

    @Test
    fun `develop branch passes gitflow validation`() {
        val violations = BranchNameValidator.validate("develop", gitflowConfig)
        assertTrue(violations.isEmpty(), "Expected no violations for develop branch")
    }

    @Test
    fun `invalid branch name fails gitflow validation`() {
        val violations = BranchNameValidator.validate("my-random-branch", gitflowConfig)
        assertEquals(1, violations.size)
        assertEquals(ViolationType.BRANCH_NAME, violations[0].type)
    }

    @Test
    fun `feature branch without name fails validation`() {
        val violations = BranchNameValidator.validate("feature/", gitflowConfig)
        assertEquals(1, violations.size)
        assertEquals(ViolationType.BRANCH_NAME, violations[0].type)
    }

    @Test
    fun `empty branch rules allow any branch name`() {
        val config = WorkflowConfig(branchRules = emptyList())
        val violations = BranchNameValidator.validate("anything-goes", config)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `base branch validation passes for correct base`() {
        val violations = BranchNameValidator.validateBaseBranch("feature/test", "develop", gitflowConfig)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `base branch validation fails for wrong base`() {
        val violations = BranchNameValidator.validateBaseBranch("feature/test", "main", gitflowConfig)
        assertEquals(1, violations.size)
        assertEquals(ViolationType.BASE_BRANCH, violations[0].type)
    }

    @Test
    fun `hotfix from main passes base branch validation`() {
        val violations = BranchNameValidator.validateBaseBranch("hotfix/urgent", "main", gitflowConfig)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `trunk-based short-lived branch passes validation`() {
        val trunkConfig = WorkflowConfigLoader.getDefaultConfig(WorkflowPreset.TRUNK_BASED)
        val violations = BranchNameValidator.validate("john/fix-bug", trunkConfig)
        assertTrue(violations.isEmpty(), "Expected no violations for trunk-based branch")
    }

    @Test
    fun `trunk-based main branch passes validation`() {
        val trunkConfig = WorkflowConfigLoader.getDefaultConfig(WorkflowPreset.TRUNK_BASED)
        val violations = BranchNameValidator.validate("main", trunkConfig)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `suggestion is provided for invalid branch`() {
        val violations = BranchNameValidator.validate("bad-branch", gitflowConfig)
        assertEquals(1, violations.size)
        assertTrue(violations[0].suggestion.isNotEmpty(), "Expected a suggestion for fix")
    }

    @Test
    fun `custom branch rules are validated correctly`() {
        val config = WorkflowConfig(
            branchRules = listOf(
                BranchRule("team-[a-z]+/.+", emptyList(), "Team branches"),
            )
        )
        val validViolations = BranchNameValidator.validate("team-alpha/feature-x", config)
        assertTrue(validViolations.isEmpty())

        val invalidViolations = BranchNameValidator.validate("other/branch", config)
        assertEquals(1, invalidViolations.size)
    }
}
