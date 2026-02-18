# Git Workflow Enforcer — Screenshot Plan

## Screenshots

### 1. Status Bar Widget — Compliant State
**What to capture:** The IDE status bar showing the green "✓ Compliant" widget. Include a portion of the editor with a properly named feature branch visible in the bottom bar.

### 2. Status Bar Widget — Violation State
**What to capture:** The IDE status bar showing a red/yellow "✗ 2 Violation(s)" widget. Show the tooltip on hover with violation details.

### 3. Pre-Commit Validation Dialog
**What to capture:** The commit dialog with a validation error popup showing a branch naming violation and a commit format violation. Show the "Cancel" and "Commit Anyway" buttons.

### 4. Settings Page — Workflow Preset Selection
**What to capture:** The Settings → Tools → Git Workflow Enforcer page showing the workflow preset dropdown (Gitflow, Trunk-Based, GitHub Flow, Custom) and all configuration options.

### 5. Violations Tool Window
**What to capture:** The bottom tool window showing violations grouped by type: Branch Naming, Commit Format, Merge Policy. Include at least 3-4 example violations.

### 6. Team Config File in Editor
**What to capture:** A `.git-workflow.yaml` file open in the editor with syntax highlighting, showing branch rules, commit rules, merge rules, and reviewer configuration.

### 7. Branch Name Validation Warning
**What to capture:** A notification balloon showing a branch naming violation with the message explaining which patterns are allowed and a suggestion to rename the branch.

### 8. Conventional Commits Completion
**What to capture:** The commit message input field showing a commit message with the Conventional Commits format, highlighting the type prefix and scope.

---

## Video Demo Outline (45 seconds)

| Time | Scene | Description |
|------|-------|-------------|
| 0:00–0:05 | Intro | Show the plugin name and tagline over a JetBrains IDE |
| 0:05–0:12 | Status bar | Point out the compliance widget in the status bar, show green state |
| 0:12–0:20 | Create bad branch | Create a branch with an invalid name, show the warning notification appear |
| 0:20–0:28 | Commit validation | Try to commit with a bad message, show the pre-commit dialog blocking it |
| 0:28–0:35 | Fix and commit | Fix the commit message to Conventional Commits format, show successful commit |
| 0:35–0:40 | Team config | Quick flash of the .git-workflow.yaml file and settings page |
| 0:40–0:45 | Outro | Show pricing tiers and call to action to install from Marketplace |
