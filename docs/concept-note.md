# Mizizi — Concept Note (Lab 1)

> Google Docs version: https://docs.google.com/document/d/1-YNjDuJa0oJdkdDgccoqha6VtPCdRiNddwuVFUeQ9pE/edit?usp=drivesdk
> (uploaded to Google Docs and shared with the lecturer — link also appears
> as the first line of `Main.kt` as required)

---

## 1. Problem Statement

Many young urban Kenyans (18–35) in Nairobi, Kisumu and Mombasa can understand
their mother tongue conversationally, but do not feel confident reading,
writing or translating it. The tools available today either give literal
translations (generic translation apps), depend on relatives who are not always
available, or are static reference books that cannot respond to the learner's
own attempt. There is no reliable, culturally aware way to attempt a word,
phrase or proverb and get feedback on whether the attempt is correct — so
mistakes go uncorrected and confidence never builds.

## 2. Proposed Solution

**Mizizi** ("roots" in Swahili) — a language-practice application built around
active practice rather than passive translation:

- The user attempts to translate a word, phrase or proverb in their own words.
- The app gives feedback based on **meaning**, not exact text matching,
  explaining why an answer is accurate, incomplete or incorrect.
- A daily proverb notification and a personal archive turn occasional
  questions into a small, repeatable learning habit.

## 3. Target Users

- Young adults aged 18–35 in Nairobi, Kisumu and Mombasa who speak English or
  Sheng most of the time but want to reconnect with their mother tongue.
- Young parents who want to teach their children familiar words, sayings and
  proverbs but are unsure of correct meanings or translations themselves.

## 4. Functionality Implemented (Lab 1)

A console-based prototype of the core feature — the **Proverb Practice Engine**:

1. **Practice session** — the app shows a proverb (Swahili), the user types
   their English translation, and the app grades the attempt
   (correct / close / incorrect) based on meaning keywords.
2. **Session score** — the user sees how many attempts they got right.
3. **Personal archive** — every attempt is recorded and can be reviewed later.
4. **Daily proverb** — the app serves a proverb of the day with its meaning.

## 5. Kotlin Concepts Used

| Concept | Where it is used |
|---------|------------------|
| Variables & constants | `const val` for app name/version, session size, pass score |
| Data types | `String`, `Int`, `Boolean`, custom classes (`Proverb`, `Attempt`) |
| Input & output | `println()` / `readln()` for all console interaction |
| Conditional statements | `if` / `when` for menu dispatch and grading tiers |
| Loops | `while` menu loop, `for` over proverbs, `for` over the archive |
| Collections | `listOf` proverb seed data, `mutableListOf` attempt archive |

## Assessment notes

The solution deliberately stays small and clear: a working, logical prototype
that demonstrates the fundamentals — not a complicated system.
