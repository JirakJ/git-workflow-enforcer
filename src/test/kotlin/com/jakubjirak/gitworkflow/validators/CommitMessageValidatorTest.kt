package com.jakubjirak.gitworkflow.validators

import com.jakubjirak.gitworkflow.model.CommitRule
import com.jakubjirak.gitworkflow.model.ViolationType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CommitMessageValidatorTest {

    private val defaultRules = CommitRule(
        maxSubjectLength = 72,
        conventionalCommits = true,
        allowedTypes = listOf("feat", "fix", "docs", "style", "refactor", "perf", "test", "build", "ci", "chore", "revert"),
    )

    @Test
    fun `valid conventional commit passes validation`() {
        val violations = CommitMessageValidator.validate("feat: add user login", defaultRules)
        assertTrue(violations.isEmpty(), "Expected no violations for valid conventional commit")
    }

    @Test
    fun `valid conventional commit with scope passes validation`() {
        val violations = CommitMessageValidator.validate("feat(auth): add login endpoint", defaultRules)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `valid fix commit passes validation`() {
        val violations = CommitMessageValidator.validate("fix: resolve null pointer in parser", defaultRules)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `invalid commit type fails validation`() {
        val violations = CommitMessageValidator.validate("invalid: some change", defaultRules)
        assertEquals(1, violations.size)
        assertEquals(ViolationType.COMMIT_FORMAT, violations[0].type)
        assertTrue(violations[0].message.contains("Invalid commit type"))
    }

    @Test
    fun `missing conventional format fails validation`() {
        val violations = CommitMessageValidator.validate("just a regular commit message", defaultRules)
        assertEquals(1, violations.size)
        assertEquals(ViolationType.COMMIT_FORMAT, violations[0].type)
    }

    @Test
    fun `subject line too long produces warning`() {
        val longSubject = "feat: " + "a".repeat(100)
        val violations = CommitMessageValidator.validate(longSubject, defaultRules)
        assertTrue(violations.any { it.message.contains("exceeds maximum length") })
    }

    @Test
    fun `empty commit message fails validation`() {
        val violations = CommitMessageValidator.validate("", defaultRules)
        assertEquals(1, violations.size)
        assertTrue(violations[0].message.contains("empty"))
    }

    @Test
    fun `whitespace only commit message fails validation`() {
        val violations = CommitMessageValidator.validate("   ", defaultRules)
        assertEquals(1, violations.size)
    }

    @Test
    fun `issue reference required and present passes validation`() {
        val rules = CommitRule(
            conventionalCommits = false,
            requireIssueRef = true,
            issueRefPattern = "[A-Z]+-\\d+",
        )
        val violations = CommitMessageValidator.validate("Fix login bug PROJECT-123", rules)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `issue reference required but missing fails validation`() {
        val rules = CommitRule(
            conventionalCommits = false,
            requireIssueRef = true,
            issueRefPattern = "[A-Z]+-\\d+",
        )
        val violations = CommitMessageValidator.validate("Fix login bug", rules)
        assertTrue(violations.any { it.message.contains("issue reference") })
    }

    @Test
    fun `format pattern validation passes for matching message`() {
        val rules = CommitRule(
            conventionalCommits = false,
            formatPattern = "\\[.*\\] .+",
        )
        val violations = CommitMessageValidator.validate("[FIX] resolve crash", rules)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `format pattern validation fails for non-matching message`() {
        val rules = CommitRule(
            conventionalCommits = false,
            formatPattern = "\\[.*\\] .+",
        )
        val violations = CommitMessageValidator.validate("no prefix here", rules)
        assertTrue(violations.any { it.message.contains("format pattern") })
    }

    @Test
    fun `non-conventional commit without enforcement passes`() {
        val rules = CommitRule(conventionalCommits = false)
        val violations = CommitMessageValidator.validate("any message format", rules)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `breaking change indicator passes validation`() {
        val violations = CommitMessageValidator.validate("feat!: breaking API change", defaultRules)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `all standard types pass validation`() {
        val types = listOf("feat", "fix", "docs", "style", "refactor", "perf", "test", "build", "ci", "chore", "revert")
        for (type in types) {
            val violations = CommitMessageValidator.validate("$type: some change", defaultRules)
            assertTrue(violations.isEmpty(), "Expected type '$type' to pass validation")
        }
    }

    @Test
    fun `subject within length limit passes`() {
        val rules = CommitRule(conventionalCommits = false, maxSubjectLength = 50)
        val violations = CommitMessageValidator.validate("Short message", rules)
        assertTrue(violations.isEmpty())
    }

    @Test
    fun `subject exactly at length limit passes`() {
        val rules = CommitRule(conventionalCommits = false, maxSubjectLength = 13)
        val violations = CommitMessageValidator.validate("Short message", rules)
        assertTrue(violations.isEmpty())
    }
}
