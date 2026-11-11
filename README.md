# Git Workflow Enforcer

[![Build](https://github.com/JirakJ/git-workflow-enforcer/actions/workflows/verify.yml/badge.svg)](https://github.com/JirakJ/git-workflow-enforcer/actions/workflows/verify.yml)
[![Version](https://img.shields.io/badge/version-2026.9.0-blue.svg)](https://github.com/JirakJ/git-workflow-enforcer/releases)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)

> **Free & open source** — every feature is unlocked for everyone, no license needed. Licensed under [Apache-2.0](LICENSE).

> Enforce team Git conventions — branch naming and commit message format — directly in your JetBrains IDE.

## ✨ Features

- **Status bar indicator** — `✓ Compliant` or `✗ N Violation(s)` for the current branch; click to refresh
- **Branch name validation** — the current branch must match one of the preset's (or your) patterns
- **Base branch check** — warns when a branch was created from a base its rule does not allow (e.g. `feature/*` from `main`)
- **Commit message validation** — subject length, Conventional Commits format with allowed types, and an optional issue reference (e.g. `JIRA-123`)
- **Pre-commit check** — violations are shown before the commit; choose **Commit Anyway** or **Cancel**
- **Violations tool window** — current violations grouped by type with fix suggestions
- **Workflow presets** — Gitflow, Trunk-Based Development, GitHub Flow and Custom
- **Team config file** — shared `.git-workflow.yaml` in the project root overrides the preset's branch and commit rules

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
5. Add a `.git-workflow.yaml` to your project root to share rules with the team (see below)

### Example `.git-workflow.yaml`

```yaml
preset: gitflow                 # gitflow | trunk_based | github_flow | custom
branches:                       # replaces the preset's branch rules
  - pattern: "feature/[A-Z]+-[0-9]+-.*"
    allowed_base: ["develop"]
    description: "Feature branches"
  - pattern: "hotfix/.+"
    allowed_base: ["main", "master"]
    description: "Hotfix branches"
  - pattern: "main|master|develop"
commits:                        # replaces the preset's commit rules
  conventional_commits: true
  max_subject_length: 72
  require_issue_ref: true
  issue_ref_pattern: "[A-Z]+-\\d+"
  allowed_types: [feat, fix, docs, style, refactor, perf, test, build, ci, chore, revert]
```

The file is read each time a check runs, so edits take effect on the next commit or status refresh.

## ⚙️ Configuration

Open **Settings** → **Tools** → **Git Workflow Enforcer**:

| Setting | Default | Description |
|---------|---------|-------------|
| Enabled | `true` | Enable or disable all checks |
| Workflow Preset | `Gitflow` | Rules used when there is no `.git-workflow.yaml` |
| Branch Validation | `true` | Check the current branch name (status bar, tool window, pre-commit) |
| Commit Validation | `true` | Run the pre-commit check |

Commit message rules come from the preset or `.git-workflow.yaml`.

## 🎯 Usage

### Workflow Presets

| Preset | Branch Patterns | Conventional Commits |
|--------|-----------------|----------------------|
| **Gitflow** | `feature/.+`, `bugfix/.+`, `release/.+`, `hotfix/.+`, `main`, `master`, `develop` | ✓ |
| **Trunk-Based** | `[a-z0-9-]+/[a-z0-9-]+`, `main` | ✗ |
| **GitHub Flow** | `[a-z0-9]+(-[a-z0-9]+)*`, `main` | ✗ |
| **Custom** | from `.git-workflow.yaml` | from `.git-workflow.yaml` |

### Pre-commit Validation

When you commit, the plugin validates the commit message and the current branch name. If there are violations, a
dialog lists errors and warnings; with errors the default button is **Cancel**, otherwise **Commit Anyway**.

### Status Bar Widget

- `✓ Compliant` — the current branch has no violations
- `✗ N Violation(s)` — hover for details, click to re-check

### Violation Types

| Type | Severity | Example |
|------|----------|---------|
| `BRANCH_NAME` | ERROR | Branch `my-branch` doesn't match any allowed pattern |
| `COMMIT_FORMAT` | ERROR/WARNING | Missing Conventional Commits prefix, subject too long, missing issue reference |
| `BASE_BRANCH` | WARNING | Feature branch created from `main` instead of `develop` |

## 📋 Requirements

- IntelliJ IDEA 2023.2+ (or another IntelliJ-based IDE, build 232+)
- Git4Idea plugin (bundled with all JetBrains IDEs)

## 🏗️ Building from Source

```bash
git clone https://github.com/JirakJ/git-workflow-enforcer.git
cd git-workflow-enforcer
./gradlew build
```

The built plugin ZIP will be in `build/distributions/`.

## License

Apache License 2.0 — see [LICENSE](LICENSE) and [NOTICE](NOTICE). Copyright 2026 Jakub Jirák.

## Contributing

Issues and pull requests are welcome at https://github.com/JirakJ/git-workflow-enforcer.

## 👤 Author

**Jakub Jirák** — [@JirakJ](https://github.com/JirakJ)
