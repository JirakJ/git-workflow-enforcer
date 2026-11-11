# Git Workflow Enforcer

[![Build](https://github.com/JirakJ/git-workflow-enforcer/actions/workflows/verify.yml/badge.svg)](https://github.com/JirakJ/git-workflow-enforcer/actions/workflows/verify.yml)
[![Version](https://img.shields.io/badge/version-2026.9.1-blue.svg)](https://github.com/JirakJ/git-workflow-enforcer/releases)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)

> **Free & open source** — every feature is unlocked for everyone, no license needed. Licensed under [Apache-2.0](LICENSE).

> Enforce team Git conventions — branch naming, commit message format and protected branches — directly in your JetBrains IDE.

## ✨ Features

- **Status bar indicator** — `✓ Compliant` or `✗ N Violation(s)` for the current branch; click to refresh
- **Branch name validation** — the current branch must match one of the preset's (or your) patterns
- **Commit message validation** — subject length, Conventional Commits format with allowed types, and an optional issue reference (e.g. `JIRA-123`)
- **Protected branches** — the pre-commit check flags committing directly on a protected branch (e.g. `main`); blocked or just warned, per your config
- **Pre-commit check** — violations are shown before the commit; choose **Commit Anyway** or **Cancel**
- **Violations tool window** — current violations grouped by type with fix suggestions
- **Workflow presets** — Gitflow, Trunk-Based Development, GitHub Flow and Custom
- **Team config file** — shared `.git-workflow.yaml` in the project root overrides the preset's branch, commit and protected-branch rules

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
    description: "Feature branches"
  - pattern: "hotfix/.+"
    description: "Hotfix branches"
  - pattern: "main|master|develop"
commits:                        # replaces the preset's commit rules
  conventional_commits: true
  max_subject_length: 72
  require_issue_ref: true
  issue_ref_pattern: "[A-Z]+-\\d+"
  allowed_types: [feat, fix, docs, style, refactor, perf, test, build, ci, chore, revert]
merge:                          # replaces the preset's protected-branch policy
  protected_branches: ["main", "develop", "release/.*"]   # exact names or regexes
  block_direct_push: true       # committing on a protected branch is an ERROR
  require_pr: true              # with block_direct_push: false, only a WARNING
```

The file is read each time a check runs, so edits take effect on the next commit or status refresh.
An `allowed_base` key on a branch rule (from older examples) is accepted but not enforced.

## ⚙️ Configuration

Open **Settings** → **Tools** → **Git Workflow Enforcer**:

| Setting | Default | Description |
|---------|---------|-------------|
| Enabled | `true` | Enable or disable all checks |
| Workflow Preset | `Gitflow` | Rules used when there is no `.git-workflow.yaml` |
| Branch Validation | `true` | Check the current branch name (status bar, tool window, pre-commit) |
| Commit Validation | `true` | Check the commit message in the pre-commit check |

Commit message rules and the protected-branch policy come from the preset or `.git-workflow.yaml`.

## 🎯 Usage

### Workflow Presets

| Preset | Branch Patterns | Conventional Commits | Protected Branches |
|--------|-----------------|----------------------|--------------------|
| **Gitflow** | `feature/.+`, `bugfix/.+`, `release/.+`, `hotfix/.+`, `main`, `master`, `develop` | ✓ | `main`, `master`, `develop` |
| **Trunk-Based** | `[a-z0-9-]+/[a-z0-9-]+`, `main` | ✗ | `main` |
| **GitHub Flow** | `[a-z0-9]+(-[a-z0-9]+)*`, `main` | ✗ | `main` |
| **Custom** | from `.git-workflow.yaml` | from `.git-workflow.yaml` | `main`, `master` unless set in `.git-workflow.yaml` |

All presets block direct commits on their protected branches (`block_direct_push: true`).

### Pre-commit Validation

When you commit, the plugin checks:

- the commit message (if **Commit Validation** is on),
- the current branch name (if **Branch Validation** is on),
- whether you are committing directly on a protected branch (always, as configured by the `merge:` section).

If there are violations, a dialog lists errors and warnings; with errors the default button is **Cancel**, otherwise
**Commit Anyway**. The protected-branch check is a commit-time check in the IDE; it does not replace server-side
branch protection.

### Status Bar Widget

- `✓ Compliant` — the current branch has no violations
- `✗ N Violation(s)` — hover for details, click to re-check

### Violation Types

| Type | Severity | Example |
|------|----------|---------|
| `BRANCH_NAME` | ERROR | Branch `my-branch` doesn't match any allowed pattern |
| `COMMIT_FORMAT` | ERROR/WARNING | Missing Conventional Commits prefix, subject too long, missing issue reference |
| `PROTECTED_BRANCH` | ERROR/WARNING | Committing directly on `main` (ERROR with `block_direct_push`, WARNING with only `require_pr`) |

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
