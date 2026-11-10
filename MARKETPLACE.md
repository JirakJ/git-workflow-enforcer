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
  <li><strong>Workflow Presets</strong> — Out-of-the-box support for Gitflow, Trunk-Based Development, GitHub Flow, and fully custom workflows.</li>
  <li><strong>Unlimited Custom Patterns</strong> — Define any branch naming regex to match your team's conventions.</li>
  <li><strong>Merge Policy Enforcement</strong> — Block direct pushes to protected branches and require pull requests.</li>
  <li><strong>Merge Direction Validation</strong> — Enforce correct merge flow (e.g., feature → develop, not feature → main).</li>
  <li><strong>Required Reviewers</strong> — CODEOWNERS-style reviewer requirements per file path with minimum approval counts.</li>
  <li><strong>Pre-Push Hook Integration</strong> — Catch violations before code leaves your machine.</li>
  <li><strong>Team Config File</strong> — Share rules via <code>.git-workflow.yaml</code> committed to your repo — new team members are compliant from clone.</li>
  <li><strong>Audit Log &amp; Compliance Dashboard</strong> — Track violations and compliance metrics across the team.</li>
</ul>

<h3>Free &amp; Open Source</h3>
<p>Every feature is free for everyone — no license, no limits. The source code is available under the Apache-2.0 license at <a href="https://github.com/JirakJ/git-workflow-enforcer">github.com/JirakJ/git-workflow-enforcer</a>. Issues and pull requests are welcome.</p>

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

Free & open source (Apache-2.0) — every feature is available to everyone, no license needed.

## Screenshots Needed
1. **Status Bar Compliance** — IDE status bar showing the real-time compliance indicator with ✓ Compliant state and ✗ Violations state side by side.
2. **Pre-Commit Validation** — Commit dialog showing a blocked commit with clear violation message explaining what needs to be fixed.
3. **Violations Tool Window** — Grouped list of violations (branch naming, commit format, merge policy) with severity icons and quick-fix actions.
4. **Workflow Presets** — Settings panel showing Gitflow, Trunk-Based, and GitHub Flow preset options with configuration preview.
5. **Team Config File** — `.git-workflow.yaml` file in the editor with branch patterns, merge policies, and reviewer rules defined.
