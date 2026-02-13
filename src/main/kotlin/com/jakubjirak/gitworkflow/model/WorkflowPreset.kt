package com.jakubjirak.gitworkflow.model

/**
 * Workflow presets defining standard Git branching strategies.
 */
enum class WorkflowPreset(val displayName: String) {
    GITFLOW("Gitflow"),
    TRUNK_BASED("Trunk-Based Development"),
    GITHUB_FLOW("GitHub Flow"),
    CUSTOM("Custom"),
}
