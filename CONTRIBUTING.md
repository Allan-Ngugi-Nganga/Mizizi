# Contributing to Mizizi

Short and sharp — read this before you touch the repo.

## Before you start

1. `git checkout develop`
2. `git pull` — make sure you're up to date
3. `git checkout -b feature/<what-you-are-building>`
   (e.g. `feature/proverb-practice`, `feature/archive`)

## While you work

- Small, logical commits with Conventional Commit messages:
  `feat:`, `fix:`, `docs:`, `refactor:`, `chore:`.
- One commit = one logical change. Commit when something works, not at midnight.
- Push your branch frequently: `git push -u origin feature/<name>`

## When you're done

1. Push your branch.
2. Open a pull request **into `develop`** (never straight into `main`).
3. Ask at least one teammate to review.
4. Merge only after the review passes.

## Hard rules

- **Never push directly to `main` or `develop`.**
- **Never force-push** a shared branch.
- Pull `develop` before starting a feature to avoid conflicts.
- If you see a merge conflict, do not "solve" it by deleting other people's
  code — talk to the person who wrote it.

## Definition of done

- Code compiles and runs.
- Console output is clear and self-explanatory.
- Every function has a short KDoc comment.
- No debug leftovers, no junk files (`.idea/`, `build/` etc. are gitignored).
