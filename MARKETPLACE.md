# Git Workflow Enforcer — JetBrains Marketplace Listing

## Plugin Name
Git Workflow Enforcer

## Tagline
Enforce Git conventions in your IDE — branch naming and commit messages.

## Short Description
Git workflow compliance in your IDE. Enforce branch naming, base branches, Conventional Commits, subject length and issue keys before you commit. Works with any Git server.

## Full Description

<p>
Every team agrees on Git conventions. Few teams follow them consistently. <strong>Git Workflow Enforcer</strong> solves this by validating your workflow in real time — right in the IDE, before bad commits happen. No more inconsistent branch names, malformed commit messages or missing JIRA keys.
</p>

<h3>Key Features</h3>
<ul>
  <li><strong>Compliance Indicator</strong> — status bar widget shows ✓ Compliant or ✗ N Violations for the current branch.</li>
  <li><strong>Branch Naming Enforcement</strong> — validate branch names against preset or custom regex patterns, including the allowed base branch.</li>
  <li><strong>Conventional Commits Validation</strong> — 11 allowed types: <code>feat</code>, <code>fix</code>, <code>docs</code>, <code>style</code>, <code>refactor</code>, <code>perf</code>, <code>test</code>, <code>build</code>, <code>ci</code>, <code>chore</code>, <code>revert</code>.</li>
  <li><strong>Subject Length &amp; Issue Key</strong> — configurable subject limit (default 72) and optional <code>[A-Z]+-[0-9]+</code> issue reference.</li>
  <li><strong>Pre-Commit Check</strong> — violations are shown before the commit with Commit Anyway / Cancel.</li>
  <li><strong>Violations Tool Window</strong> — current violations grouped by type with fix suggestions.</li>
  <li><strong>Workflow Presets</strong> — Gitflow, Trunk-Based Development, GitHub Flow and Custom.</li>
  <li><strong>Team Config File</strong> — share rules via <code>.git-workflow.yaml</code> committed to your repo.</li>
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
<p>Works with <strong>IntelliJ IDEA 2023.2+</strong> and all JetBrains IDEs with Git support (WebStorm, PyCharm, GoLand, Rider, PhpStorm, CLion, RubyMine, Android Studio). <strong>Server-agnostic</strong> — works with GitHub, GitLab, Bitbucket, Azure DevOps, and self-hosted Git servers. Cross-platform: macOS, Linux, Windows.</p>

<h3>Part of Developer Productivity Suite</h3>
<p>Git Workflow Enforcer is part of a growing suite of JetBrains plugins designed to supercharge your development workflow. Pair it with <strong>Git-Aware Dev Journal</strong>, <strong>Commit Message Copilot</strong>, <strong>Branch Health Dashboard</strong>, and <strong>Technical Debt Accountant</strong> for a complete productivity toolkit.</p>

## Tags
git, workflow, conventional-commits, branch-naming, code-review

## Category
Version Control

## Pricing

Free & open source (Apache-2.0) — every feature is available to everyone, no license needed.

## Screenshots
1. **Status bar** — the compliance indicator in ✓ and ✗ states.
2. **Pre-commit dialog** — a commit with a malformed message and the Commit Anyway / Cancel choice.
3. **Violations tool window** — violations grouped by type with suggestions.
4. **Settings** — preset selection and validation toggles.
