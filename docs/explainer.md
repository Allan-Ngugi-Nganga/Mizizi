# Mizizi Main.kt — Plain-English Explainer

For the demo: read this before the lecturer asks anything. **Every group
member should be able to explain every line** — "ability to explain your
implementation" is a rubric item.

## The big picture

The program is a menu-driven console app. It shows a menu, waits for a number,
does what the number says, then shows the menu again. Three features:
practice session, personal archive, daily proverb. All data lives in memory
for this run — no database, which is fine for Lab 1.

## Constructs, one by one

**1. Header comments (lines 1–9)**
The Google Docs concept note link is the *first line* (assignment requirement),
then the group number and all 7 members with admission numbers.

**2. Classes `Proverb` and `Attempt`**
Blueprints for data. `Proverb` bundles what belongs together: the Swahili
text, its English meaning, and the keywords that prove understanding.
`Attempt` records one try: which proverb, what the user typed, and the grade.
Classes give the data structure instead of loose variables flying around.

**3. Constants (`const val`)**
Values that never change: app name, version, 5 proverbs per session, pass
score of 3. Magic numbers appear once, named, and are easy to change.

**4. Seed data (`val proverbs = listOf(...)`)**
The collection of 8 proverbs — our "database" for now. `val` means the list
itself can't be reassigned.

**5. `fun main()`**
Every Kotlin program starts here. It creates the archive
(`mutableListOf<Attempt>()` — an empty list that *can* grow), prints the
banner, then enters the menu loop.

**6. `while (true)` loop + `when`**
The menu loop runs until `return` (choice 4). `when` is Kotlin's smarter
switch: 1 → practice, 2 → archive, 3 → daily proverb, 4 → exit, anything else
→ "Invalid choice". This is the *conditionals* the rubric asks for.

**7. `showMenu()` and safe input**
`readlnOrNull()?.trim()?.toIntOrNull() ?: -1`: read a line, trim it, try to
turn it into a number. If the user types "abc", `toIntOrNull()` gives `null`,
and `?: -1` (the Elvis operator) turns it into -1 so the menu prints "Invalid
choice" instead of crashing. Bulletproof input is a quality mark.

**8. `startPractice()` — the heart of the app**
- `proverbs.shuffled().take(5)` — shuffle, pick 5 random proverbs. Same code,
  different session every time.
- `for ((index, proverb) in sessionProverbs.withIndex())` — loop over the 5,
  tracking the position. The *loops + collections* combo.
- For each: print the original, read the answer, grade it, save to archive,
  print feedback.

**9. `gradeAttempt()` — the "meaning-based" brain**
Normalizes both texts (lowercase, punctuation stripped via `Regex`); exact
match → CORRECT. Otherwise counts how many key meaning words appear in the
answer: all → CORRECT, at least one → CLOSE, none → INCORRECT. This is the
stand-in for the LLM feedback engine the full app will use. Demonstrates
`if`, `when` and `count { }`.

**10. `viewArchive()`**
Loops the `mutableListOf` attempts and prints each one — the personal-record
feature.

**11. `showDailyProverb()`**
Picks a random proverb with `Random.nextInt` and prints it with its meaning.

**12. KDoc comments (`/** ... */`)**
Standard Kotlin documentation above every function — also a good talking
point if the lecturer asks why the code is organised.

## How to answer "why is it organised this way?"

> "Each feature is its own function — the menu only decides which function to
> call. That keeps the code readable and testable, and adding a feature means
> adding one function and one menu option."

## Map to the rubric

| Rubric item | Where it lives |
|---|---|
| Variables & constants | `const val` block, `val`/`var` |
| Data types | `String`, `Int`, `Boolean`, `List`, classes |
| Input & output | `println()` / `readln()` everywhere |
| Conditionals | `when` menu, `if`/`when` grading |
| Loops | `while` menu loop, `for` sessions & archive |
| Collections | `listOf` proverbs, `mutableListOf` archive |
