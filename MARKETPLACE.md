# Git Workflow Enforcer — JetBrains Marketplace Listing

## Plugin Name
Git Workflow Enforcer

## Tagline
Enforce Git conventions in real time — branch naming, commits, and merge policies.

## Short Description
Real-time Git workflow compliance in your IDE. Enforce branch naming, conventional commits, merge policies, and code review requirements. Works with any Git server.

## Full Description

<p>
Every team agrees on Git conventions. Few teams follow them consistently. <strong>Git Workflow Enforcer</strong> solves this by validating your workflow in real time — right in the IDE, before bad commits or pushes ever happen. No more inconsistent branch names, missing JIRA keys, direct pushes to main, or skipped code reviews.
</p>

<h3>Key Features</h3>
<ul>
  <li><strong>Real-Time Compliance Indicator</strong> — Status bar widget shows ✓ Compliant or ✗ N Violations at a glance, updated live as you work.</li>
  <li><strong>Branch Naming Enforcement</strong> — Validate branch names against patterns (feature/, bugfix/, hotfix/) with instant feedback when rules are broken.</li>
  <li><strong>Conventional Commits Validation</strong> — Enforces the Conventional Commits spec with 11 allowed types: <code>feat</code>, <code>fix</code>, <code>docs</code>, <code>style</code>, <code>refactor</code>, <code>perf</code>, <code>test</code>, <code>build</code>, <code>ci</code>, <code>chore</code>, <code>revert</code>.</li>
  <li><strong>Commit Message Length Validation</strong> — Configurable subject line limit (default: 72 chars) to keep messages readable.</li>
  <li><strong>JIRA Key Requirement</strong> — Ensures every commit references a JIRA ticket (<code>[A-Z]+-[0-9]+</code>).</li>
  <li><strong>Pre-Commit Validation</strong> — Blocks or warns on non-compliant commits before they enter your history.</li>
  <li><strong>Violations Tool Window</strong> — Grouped, searchable list of all current violations with quick-fix suggestions.</li>
  <li><strong>Workflow Presets</strong> — Out-of-the-box support for Gitflow, Trunk-Based Development, GitHub Flow, and fully custom workflows. <em>(Enterprise)</em></li>
  <li><strong>Unlimited Custom Patterns</strong> — Define any branch naming regex to match your team's conventions. <em>(Enterprise)</em></li>
  <li><strong>Merge Policy Enforcement</strong> — Block direct pushes to protected branches and require pull requests. <em>(Enterprise)</em></li>
  <li><strong>Merge Direction Validation</strong> — Enforce correct merge flow (e.g., feature → develop, not feature → main). <em>(Enterprise)</em></li>
  <li><strong>Required Reviewers</strong> — CODEOWNERS-style reviewer requirements per file path with minimum approval counts. <em>(Enterprise)</em></li>
  <li><strong>Pre-Push Hook Integration</strong> — Catch violations before code leaves your machine. <em>(Enterprise)</em></li>
  <li><strong>Team Config File</strong> — Share rules via <code>.git-workflow.yaml</code> committed to your repo — new team members are compliant from clone. <em>(Enterprise)</em></li>
  <li><strong>Audit Log &amp; Compliance Dashboard</strong> — Track violations and compliance metrics across the team. <em>(Enterprise)</em></li>
</ul>

<h3>Free Tier</h3>
<p>Essential workflow enforcement at no cost:</p>
<ul>
  <li>Status bar compliance indicator (real-time)</li>
  <li>3 default branch naming patterns (feature/, bugfix/, hotfix/)</li>
  <li>Commit message length validation</li>
  <li>Conventional Commits format enforcement</li>
  <li>JIRA key requirement validation</li>
  <li>Pre-commit validation (block/warn)</li>
  <li>Violations tool window with grouping</li>
</ul>

<h3>Enterprise Tier — $15/dev/month (or $150/dev/year)</h3>
<ul>
  <li>Everything in Free</li>
  <li>All workflow presets (Gitflow, Trunk-Based, GitHub Flow, Custom)</li>
  <li>Unlimited custom regex patterns for branch naming</li>
  <li>Merge policy enforcement — block direct pushes, require PRs</li>
  <li>Merge direction validation</li>
  <li>Required reviewers per file path (CODEOWNERS-style)</li>
  <li>Pre-push hook integration</li>
  <li>Team config file (<code>.git-workflow.yaml</code>) — version-controlled rules</li>
  <li>Audit log and compliance dashboard</li>
</ul>

<h3>Getting Started</h3>
<ol>
  <li><strong>Install</strong> — Search "Git Workflow Enforcer" in <em>Settings → Plugins → Marketplace</em> and click Install.</li>
  <li><strong>Check the Status Bar</strong> — Look for the compliance indicator in the bottom-right. Green ✓ means you're good; red ✗ means there are violations to fix.</li>
  <li><strong>Commit with Confidence</strong> — The plugin validates your commit message and branch name automatically. Non-compliant commits are blocked with clear guidance on how to fix them.</li>
</ol>

<h3>Compatibility</h3>
<p>Works with <strong>IntelliJ IDEA 2024.3+</strong> and all JetBrains IDEs with Git support (WebStorm, PyCharm, GoLand, Rider, PhpStorm, CLion, RubyMine, Android Studio). <strong>Server-agnostic</strong> — works with GitHub, GitLab, Bitbucket, Azure DevOps, and self-hosted Git servers. Cross-platform: macOS, Linux, Windows.</p>

<h3>Part of Developer Productivity Suite</h3>
<p>Git Workflow Enforcer is part of a growing suite of JetBrains plugins designed to supercharge your development workflow. Pair it with <strong>Git-Aware Dev Journal</strong>, <strong>Commit Message Copilot</strong>, <strong>Branch Health Dashboard</strong>, and <strong>Technical Debt Accountant</strong> for a complete productivity toolkit.</p>

## Tags
git, workflow, conventional-commits, branch-naming, code-review

## Category
Version Control

## Pricing
| Tier | Price | Features |
|------|-------|----------|
| Free | $0 | Status bar compliance, 3 branch patterns, commit length validation, conventional commits, JIRA key check, pre-commit validation, violations window |
| Enterprise | $15/dev/month or $150/dev/year | Workflow presets, unlimited patterns, merge policies, merge direction, required reviewers, pre-push hooks, team config, audit log, dashboard |

## Screenshots Needed
1. **Status Bar Compliance** — IDE status bar showing the real-time compliance indicator with ✓ Compliant state and ✗ Violations state side by side.
2. **Pre-Commit Validation** — Commit dialog showing a blocked commit with clear violation message explaining what needs to be fixed.
3. **Violations Tool Window** — Grouped list of violations (branch naming, commit format, merge policy) with severity icons and quick-fix actions.
4. **Workflow Presets** — Settings panel showing Gitflow, Trunk-Based, and GitHub Flow preset options with configuration preview.
5. **Team Config File** — `.git-workflow.yaml` file in the editor with branch patterns, merge policies, and reviewer rules defined.
