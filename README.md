# Git Workflow Enforcer

[![Build](https://github.com/JirakJ/git-workflow-enforcer/actions/workflows/build.yml/badge.svg)](https://github.com/JirakJ/git-workflow-enforcer/actions/workflows/build.yml)
[![Version](https://img.shields.io/badge/version-2026.2.1-blue.svg)](https://github.com/JirakJ/git-workflow-enforcer/releases)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

> Enforce team Git conventions — branch naming, commit format, and merge policies — directly in your JetBrains IDE.

## ✨ Features

- **Status bar indicator** — real-time compliance widget: `✓ Compliant` (green) or `✗ N Violation(s)` (red/yellow)
- **Branch name validation** — 3 default patterns (feature, bugfix, hotfix) with violation warnings
- **Commit message length check** — configurable max subject length (default 72 characters)
- **Conventional Commits** — enforce `<type>(<scope>): <description>` format with 11 allowed types
- **JIRA key validation** — require issue references matching `[A-Z]+-[0-9]+` in commit messages
- **Pre-commit validation** — checkin handler blocks or warns on violations before commit
- **Violations tool window** — grouped by type (Branch Naming / Commit Format / Merge Policy / Base Branch)
- **Basic notifications** — warning and error balloons with fix suggestions
- **All workflow presets** — Gitflow, Trunk-Based Development, GitHub Flow, and Custom
- **Custom regex rules** — define your own branch naming patterns with allowed base branches
- **Merge policy enforcement** — block direct pushes to protected branches, require pull requests
- **Merge direction validation** — ensure branches merge to the correct target (e.g., feature → develop)
- **Required reviewers per path** — CODEOWNERS-like rules with minimum approval counts
- **Pre-push validation hook** — validate branch names, commit messages, and merge policy before push
- **Team config file** — shared `.git-workflow.yaml` versioned in Git with auto-reload
- **Audit log** — track all violations and overrides across the team
- **Compliance dashboard** — team-wide compliance metrics and trends

## 📦 Installation

### From JetBrains Marketplace
1. Open **Settings** → **Plugins** → **Marketplace**
2. Search for **Git Workflow Enforcer**
3. Click **Install** and restart the IDE

### From ZIP
1. Download the latest release from [Releases](https://github.com/JirakJ/git-workflow-enforcer/releases)
2. Open **Settings** → **Plugins** → ⚙️ → **Install Plugin from Disk…**
3. Select the downloaded ZIP file and restart the IDE

## 🚀 Quick Start

1. Open a project with a Git repository
2. The status bar widget appears automatically — check your current compliance status
3. Open **Settings** → **Tools** → **Git Workflow Enforcer** to select a workflow preset
4. Create a branch or commit — the plugin validates in real-time
5. Add a `.git-workflow.yaml` to your project root to share rules with the team

### Example `.git-workflow.yaml`

```yaml
preset: gitflow
branchRules:
  - pattern: "feature/[A-Z]+-[0-9]+-.*"
    allowedBase: ["develop"]
    description: "Feature branches"
  - pattern: "bugfix/[A-Z]+-[0-9]+-.*"
    allowedBase: ["develop"]
    description: "Bugfix branches"
  - pattern: "hotfix/[A-Z]+-[0-9]+-.*"
    allowedBase: ["main", "master"]
    description: "Hotfix branches"
  - pattern: "release/[0-9]+\\.[0-9]+\\.[0-9]+"
    allowedBase: ["develop"]
    description: "Release branches"
commitRules:
  conventionalCommits: true
  maxSubjectLength: 72
  requireIssueRef: true
  issueRefPattern: "[A-Z]+-\\d+"
  allowedTypes:
    - feat
    - fix
    - docs
    - style
    - refactor
    - perf
    - test
    - build
    - ci
    - chore
    - revert
mergeRules:
  protectedBranches: ["main", "master", "develop"]
  requirePR: true
  blockDirectPush: true
reviewers:
  - path: "src/main/kotlin/com/*/security/**"
    required: ["@security-team"]
    min_approvals: 2
  - path: "*.gradle.kts"
    required: ["@build-team"]
    min_approvals: 1
```

## ⚙️ Configuration

Open **Settings** → **Tools** → **Git Workflow Enforcer** to configure:

| Setting | Default | Description |
|---------|---------|-------------|
| Enabled | `true` | Enable or disable the plugin globally |
| Workflow Preset | `Gitflow` | Workflow preset: Gitflow, Trunk-Based, GitHub Flow, or Custom |
| Branch Validation | `true` | Validate branch names against preset patterns |
| Commit Validation | `true` | Validate commit messages on commit |
| Merge Validation | `true` | Validate merge targets against protected branch rules |
| Conventional Commits | `true` | Require Conventional Commits format (`type(scope): description`) |
| Require Issue Reference | `false` | Require JIRA/issue key in commit messages |
| Max Subject Length | `72` | Maximum number of characters for the commit subject line |
| Protected Branches | `main,master,develop` | Comma-separated list of branches protected from direct pushes |

## 🎯 Usage

### Workflow Presets

| Preset | Branch Patterns | Protected Branches | Conventional Commits |
|--------|----------------|--------------------|--------------------|
| **Gitflow** | `feature/.+`, `bugfix/.+`, `release/.+`, `hotfix/.+` | main, master, develop | ✓ |
| **Trunk-Based** | `[a-z0-9-]+/[a-z0-9-]+` | main | ✗ |
| **GitHub Flow** | `[a-z0-9]+(-[a-z0-9]+)*` | main | ✗ |
| **Custom** | User-defined regex | User-defined | Configurable |

### Pre-commit Validation

When you commit, the plugin validates your commit message and branch name. If violations are found:

- **Errors** (red) — shown in a dialog; default action is **Cancel**
- **Warnings** (yellow) — shown in a dialog; you can choose **Commit Anyway**

### Status Bar Widget

The bottom status bar shows real-time compliance:
- `✓ Compliant` — no violations detected (green)
- `⚠ N Violation(s)` — warnings or errors found (yellow/red)

Click the widget to refresh the compliance check. Hover for a tooltip with violation details.

### Violation Types

| Type | Severity | Example |
|------|----------|---------|
| `BRANCH_NAME` | ERROR | Branch `my-branch` doesn't match any allowed pattern |
| `COMMIT_FORMAT` | ERROR/WARNING | Missing Conventional Commits prefix or subject too long |
| `MERGE_POLICY` | ERROR | Direct push to protected branch `main` |
| `BASE_BRANCH` | WARNING | Feature branch created from `main` instead of `develop` |

## 📋 Requirements

- IntelliJ IDEA 2024.3+ (or any JetBrains IDE build 243–252.*)
- Git4Idea plugin (bundled with all JetBrains IDEs)
- Java 17+

## 🏗️ Building from Source

```bash
git clone https://github.com/JirakJ/git-workflow-enforcer.git
cd git-workflow-enforcer
./gradlew build
```

The built plugin ZIP will be in `build/distributions/`.

## 📝 License

MIT — [LICENSE](LICENSE)

## 👤 Author

**Jakub Jirák** — [@JirakJ](https://github.com/JirakJ)
