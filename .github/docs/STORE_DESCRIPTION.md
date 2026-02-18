<h2>Git Workflow Enforcer — Automatic Branch, Commit, and Merge Validation</h2>

<p>
Stop catching Git convention violations in code reviews. Git Workflow Enforcer validates branch names, commit messages, and merge policies <b>before</b> commits happen — keeping your Git history clean and your team consistent.
</p>

<h3>Branch Naming</h3>
<ul>
  <li>Enforce Gitflow, trunk-based, GitHub Flow, or custom naming patterns</li>
  <li>Real-time validation with clear error messages</li>
  <li>Configurable regex patterns for any naming convention</li>
</ul>

<h3>Commit Message Validation</h3>
<ul>
  <li>Conventional Commits format enforcement</li>
  <li>Configurable maximum subject length (default 72 characters)</li>
  <li>JIRA/issue key requirement in commit messages</li>
  <li>11 allowed commit types: feat, fix, docs, style, refactor, perf, test, build, ci, chore, revert</li>
</ul>

<h3>Merge and Push Policies</h3>
<ul>
  <li>Block direct pushes to protected branches (main, master, develop)</li>
  <li>Enforce pull request workflows</li>
  <li>Merge direction validation (feature → develop, hotfix → main)</li>
  <li>Required reviewers per file path (CODEOWNERS-like rules)</li>
</ul>

<h3>IDE Integration</h3>
<ul>
  <li>Status bar widget with real-time compliance status</li>
  <li>Pre-commit validation with block/warn options</li>
  <li>Violations tool window grouped by type</li>
  <li>Balloon notifications with fix suggestions</li>
</ul>

<h3>Team Configuration</h3>
<ul>
  <li>Shared <code>.git-workflow.yaml</code> versioned in Git</li>
  <li>Auto-reload when config file changes</li>
  <li>Workflow presets: Gitflow, Trunk-Based, GitHub Flow, Custom</li>
</ul>

<h3>Pricing</h3>
<ul>
  <li><b>Free:</b> Branch naming, commit validation, status bar widget, pre-commit checks</li>
  <li><b>Enterprise ($15/dev/month):</b> Custom regex rules, merge policies, audit log, compliance dashboard, team config</li>
</ul>

<h3>Requirements</h3>
<ul>
  <li>IntelliJ IDEA 2024.3+ (or any JetBrains IDE build 243–252.*)</li>
  <li>Git4Idea plugin (bundled with all JetBrains IDEs)</li>
  <li>Java 17+</li>
</ul>

<h3>Support</h3>
<ul>
  <li>GitHub: <a href="https://github.com/JirakJ/git-workflow-enforcer">github.com/JirakJ/git-workflow-enforcer</a></li>
  <li>Issues: <a href="https://github.com/JirakJ/git-workflow-enforcer/issues">Report a Bug</a></li>
  <li>Author: Jakub Jirák</li>
</ul>
