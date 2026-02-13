package com.jakubjirak.gitworkflow.validators

import com.jakubjirak.gitworkflow.model.CommitRule
import com.jakubjirak.gitworkflow.model.Severity
import com.jakubjirak.gitworkflow.model.Violation
import com.jakubjirak.gitworkflow.model.ViolationType
import com.intellij.openapi.diagnostic.Logger

/**
 * Validates commit messages against configured format rules.
 * Supports Conventional Commits, issue references, and length limits.
 */
object CommitMessageValidator {

    private val LOG = Logger.getInstance(CommitMessageValidator::class.java)
    private val CONVENTIONAL_COMMIT_REGEX = Regex("^(\\w+)(\\(.+\\))?!?: .+")

    fun validate(message: String, rules: CommitRule): List<Violation> {
        val violations = mutableListOf<Violation>()
        val lines = message.lines()
        val subject = lines.firstOrNull()?.trim() ?: ""

        if (subject.isEmpty()) {
            violations.add(
                Violation(
                    type = ViolationType.COMMIT_FORMAT,
                    severity = Severity.ERROR,
                    message = "Commit message cannot be empty",
                )
            )
            return violations
        }

        validateSubjectLength(subject, rules, violations)
        validateConventionalCommits(subject, rules, violations)
        validateIssueRef(message, rules, violations)
        validateFormatPattern(message, rules, violations)

        return violations
    }

    private fun validateSubjectLength(subject: String, rules: CommitRule, violations: MutableList<Violation>) {
        if (subject.length > rules.maxSubjectLength) {
            violations.add(
                Violation(
                    type = ViolationType.COMMIT_FORMAT,
                    severity = Severity.WARNING,
                    message = "Subject line exceeds maximum length of ${rules.maxSubjectLength} characters " +
                        "(current: ${subject.length})",
                    suggestion = "Shorten the subject line to ${rules.maxSubjectLength} characters or less",
                )
            )
        }
    }

    private fun validateConventionalCommits(subject: String, rules: CommitRule, violations: MutableList<Violation>) {
        if (!rules.conventionalCommits) return

        val match = CONVENTIONAL_COMMIT_REGEX.matchEntire(subject)
        if (match == null) {
            violations.add(
                Violation(
                    type = ViolationType.COMMIT_FORMAT,
                    severity = Severity.ERROR,
                    message = "Commit must use Conventional Commits format: <type>(<scope>): <description>",
                    suggestion = "Example: feat(auth): add login endpoint",
                )
            )
            return
        }

        val type = match.groupValues[1]
        if (type !in rules.allowedTypes) {
            val allowed = rules.allowedTypes.joinToString(", ")
            violations.add(
                Violation(
                    type = ViolationType.COMMIT_FORMAT,
                    severity = Severity.ERROR,
                    message = "Invalid commit type \"$type\". Allowed types: $allowed",
                    suggestion = "Use one of: $allowed",
                )
            )
        }
    }

    private fun validateIssueRef(message: String, rules: CommitRule, violations: MutableList<Violation>) {
        if (!rules.requireIssueRef) return

        try {
            val issueRegex = Regex(rules.issueRefPattern)
            if (!issueRegex.containsMatchIn(message)) {
                violations.add(
                    Violation(
                        type = ViolationType.COMMIT_FORMAT,
                        severity = Severity.WARNING,
                        message = "Commit message must include an issue reference matching: ${rules.issueRefPattern}",
                        suggestion = "Add an issue key, e.g. PROJECT-123",
                    )
                )
            }
        } catch (e: Exception) {
            LOG.warn("Invalid issue reference pattern: ${rules.issueRefPattern}", e)
        }
    }

    private fun validateFormatPattern(message: String, rules: CommitRule, violations: MutableList<Violation>) {
        if (rules.formatPattern.isEmpty()) return

        try {
            val formatRegex = Regex(rules.formatPattern)
            if (!formatRegex.matches(message.lines().first().trim())) {
                violations.add(
                    Violation(
                        type = ViolationType.COMMIT_FORMAT,
                        severity = Severity.ERROR,
                        message = "Commit message does not match required format pattern: ${rules.formatPattern}",
                    )
                )
            }
        } catch (e: Exception) {
            LOG.warn("Invalid format pattern: ${rules.formatPattern}", e)
        }
    }
}
