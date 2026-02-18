# Git Workflow Enforcer

## Consistent Git Workflows, Enforced Automatically

> Stop relying on code reviews to catch naming violations. Git Workflow Enforcer validates branch names, commit messages, and merge policies before commits happen.

---

## The Problem

Teams waste hours in code reviews catching simple Git convention violations. Branch names don't follow patterns. Commit messages lack issue references. Developers push directly to protected branches. Manual enforcement doesn't scale — and it creates friction between teammates.

---

## Key Features

### Branch Name Validation
Enforce Gitflow, trunk-based, GitHub Flow, or fully custom naming conventions. The plugin checks your branch name against configurable regex patterns and warns you immediately when something doesn't match.

### Commit Message Checks
Validate Conventional Commits format (`type(scope): description`), enforce maximum subject length, and require JIRA/issue key references in every commit message.

### Pre-Commit Validation
A checkin handler runs before every commit. Non-compliant commits are blocked with clear error messages, or shown as warnings you can override.

### Status Bar Widget
A real-time compliance indicator sits in your IDE status bar — green when everything is fine, red when violations are detected. Click to refresh or hover for details.

### Merge Policy Enforcement
Block direct pushes to protected branches like `main`, `master`, and `develop`. Require pull requests and validate merge direction (e.g., feature branches merge only to develop).

### Team Config File
Share rules across the entire team with a `.git-workflow.yaml` file versioned in your repository. The plugin auto-reloads when the file changes.

### Workflow Presets
Choose from Gitflow, Trunk-Based Development, GitHub Flow, or define a fully custom workflow with your own patterns and rules.

### Violations Tool Window
View all detected violations in a dedicated tool window, grouped by type: branch naming, commit format, merge policy, and base branch violations.

---

## How It Works

1. **Install the plugin** — Available from the JetBrains Marketplace. Works with any JetBrains IDE.
2. **Select a workflow preset** — Open Settings → Tools → Git Workflow Enforcer and choose Gitflow, Trunk-Based, GitHub Flow, or Custom.
3. **Work as usual** — The plugin validates in real-time. Branch names are checked when created, commit messages are validated before commit.
4. **Share with the team** — Drop a `.git-workflow.yaml` in your repo root and every team member gets the same rules automatically.

---

## Pricing

### Free Tier — $0
- Status bar compliance indicator
- Branch name validation (3 default patterns)
- Commit message length check
- Conventional Commits enforcement
- JIRA key validation
- Pre-commit validation
- Violations tool window

### Enterprise Tier — $15/dev/month
- All workflow presets
- Custom regex rules
- Merge policy enforcement
- Merge direction validation
- Required reviewers per path
- Pre-push validation hook
- Team config file with auto-reload
- Audit log and compliance dashboard

---

## Testimonials

> *"We reduced our code review turnaround time by 30% after adopting Git Workflow Enforcer. No more back-and-forth about branch naming."*
> — **Engineering Lead, SaaS Company**

> *"The production branch protection alone has saved us from at least three accidental deployments."*
> — **DevOps Engineer, FinTech Startup**

> *"Finally, a tool that makes Git conventions automatic instead of optional."*
> — **Senior Developer, Enterprise Team**

---

## FAQ

**Q: Which JetBrains IDEs are supported?**
A: Any JetBrains IDE based on IntelliJ Platform 2024.3 or later — IntelliJ IDEA, WebStorm, PyCharm, GoLand, Rider, and more.

**Q: Does it work with existing Git hooks?**
A: Yes. The plugin runs its own validation independently of Git hooks, so they complement each other without conflicts.

**Q: Can I override a validation warning?**
A: Yes. Warnings show a "Commit Anyway" option. Errors require fixing before the commit proceeds.

**Q: How do I share rules with my team?**
A: Create a `.git-workflow.yaml` file in your project root and commit it to your repository. Every team member with the plugin installed will use the same rules.

**Q: Does the Free tier have any time limit?**
A: No. The Free tier is free forever with no expiration.

---

## Get Started

Install Git Workflow Enforcer from the JetBrains Marketplace today and bring consistency to your team's Git workflow in minutes.

[Install from Marketplace](https://plugins.jetbrains.com) | [View on GitHub](https://github.com/JirakJ/git-workflow-enforcer)
