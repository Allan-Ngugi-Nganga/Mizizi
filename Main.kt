// Concept Note: https://docs.google.com/document/PASTE_YOUR_LINK_HERE

// Group <N> - Mizizi: Keeping Kenyan languages usable for the next generation
// Allan Ngugi - 191250
// Ziki Mtula - 192992
// Michael Asingwire - 192483
// Kelly Wambui - 190500
// Daniella Thande - 152581
// Evelyn Mwangi - 190051
// Daniel Karanja - 180761

import kotlin.random.Random

/**
 * A single proverb the user can practise translating.
 *
 * @property original the proverb written in the mother tongue (Swahili)
 * @property meaning   the English meaning/translation of the proverb
 * @property keywords  key English words that show the user understood the meaning
 */
class Proverb(
    val original: String,
    val meaning: String,
    val keywords: List<String>
)

/**
 * One recorded attempt made by the user during a practice session.
 *
 * @property proverb    the proverb that was attempted
 * @property userAnswer what the user typed
 * @property result     the grade received: CORRECT, CLOSE or INCORRECT
 */
class Attempt(
    val proverb: Proverb,
    val userAnswer: String,
    val result: String
)

// ---------- Constants ----------

const val APP_NAME = "MIZIZI"
const val VERSION = "1.0.0"
const val PROVERBS_PER_SESSION = 5
const val PASS_SCORE = 3

// ---------- Seed data: well-known Swahili proverbs ----------

val proverbs = listOf(
    Proverb(
        "Haraka haraka haina baraka",
        "Haste makes waste",
        listOf("haste", "hurry", "waste", "blessings")
    ),
    Proverb(
        "Asiyekujua hakuthamini",
        "He who does not know you does not value you",
        listOf("know", "value", "appreciate")
    ),
    Proverb(
        "Mvumilivu hula mbivu",
        "The patient one eats ripe fruit",
        listOf("patient", "patience", "ripe", "fruit")
    ),
    Proverb(
        "Mtaka yote hukosa yote",
        "He who wants everything loses everything",
        listOf("everything", "wants", "loses", "all")
    ),
    Proverb(
        "Samaki mkunje angali mbichi",
        "Bend the fish while it is still fresh",
        listOf("bend", "fresh", "young", "teach")
    ),
    Proverb(
        "Akili nyingi huondoa maarifa",
        "Too much cunning removes wisdom",
        listOf("cunning", "wisdom", "clever")
    ),
    Proverb(
        "Fadhili ni rehani",
        "A favour is a loan that must be repaid",
        listOf("favour", "favor", "loan", "repay")
    ),
    Proverb(
        "Penye nia pana njia",
        "Where there is a will, there is a way",
        listOf("will", "way", "determination")
    )
)

// ---------- Main program ----------

fun main() {
    // The user's personal archive: every attempt made this run.
    val archive = mutableListOf<Attempt>()

    printBanner()

    // The main menu loop: keeps showing the menu until the user chooses Exit.
    while (true) {
        val choice = showMenu()
        when (choice) {
            1 -> startPractice(proverbs, archive)
            2 -> viewArchive(archive)
            3 -> showDailyProverb(proverbs)
            4 -> {
                println(DIVIDER)
                println("Asante! Karibu tena. (Thank you! Come again.)")
                println(DIVIDER)
                return
            }
            else -> println("Invalid choice. Please enter a number from 1 to 4.")
        }
    }
}

/** Prints the opening banner so the user knows what system they are running. */
fun printBanner() {
    println("========================================")
    println("        $APP_NAME - PROVERB PRACTICE")
    println("========================================")
    println("Keeping Kenyan languages usable for the next generation.")
    println("Practise translating proverbs and get feedback on your attempt.")
    println("Version $VERSION")
    println("========================================")
}

/**
 * Shows the main menu and reads the user's choice.
 *
 * @return the chosen option as an Int, or -1 if the input was not a number
 */
fun showMenu(): Int {
    println(DIVIDER)
    println("MAIN MENU")
    println(DIVIDER)
    println("1. Start a practice session")
    println("2. View my archive")
    println("3. Daily proverb")
    println("4. Exit")
    print("Enter your choice: ")
    return readlnOrNull()?.trim()?.toIntOrNull() ?: -1
}

/**
 * Runs one practice session: a random set of proverbs, one at a time.
 * The user translates each proverb and receives meaning-based feedback.
 * Every attempt is saved to the archive.
 *
 * @param proverbs the full collection of proverbs available
 * @param archive  the user's personal archive of attempts
 */
fun startPractice(proverbs: List<Proverb>, archive: MutableList<Attempt>) {
    println(DIVIDER)
    println("PRACTICE SESSION")
    println(DIVIDER)
    println("You will see ${PROVERBS_PER_SESSION} proverbs. Translate each one into English.")
    println("The feedback is based on meaning, not exact wording.")

    // Pick a random set of proverbs for this session.
    val sessionProverbs = proverbs.shuffled().take(PROVERBS_PER_SESSION)
    var score = 0

    for ((index, proverb) in sessionProverbs.withIndex()) {
        println(DIVIDER)
        println("Proverb ${index + 1} of ${sessionProverbs.size}")
        println("Original: ${proverb.original}")
        print("Your translation: ")
        val answer = readln().trim()

        val result = gradeAttempt(proverb, answer)
        archive.add(Attempt(proverb, answer, result))

        when (result) {
            "CORRECT" -> {
                println("[CORRECT] Excellent! Your translation captures the meaning.")
                score++
            }
            "CLOSE" -> {
                println("[CLOSE] You are on the right track!")
                println("Meaning: ${proverb.meaning}")
            }
            else -> {
                println("[INCORRECT] Not quite. A good translation would be:")
                println("Meaning: ${proverb.meaning}")
            }
        }
    }

    println(DIVIDER)
    println("SESSION COMPLETE")
    println(DIVIDER)
    println("Your score: $score out of ${sessionProverbs.size}")
    if (score >= PASS_SCORE) {
        println("Well done - you are building confidence!")
    } else {
        println("Keep practising - every attempt counts.")
    }
    println(DIVIDER)
}

/**
 * Grades the user's translation attempt based on meaning.
 *
 * The answer is compared to the official meaning and checked for the
 * proverb's key meaning words. This is a simple stand-in for the
 * meaning-based feedback engine that the full app will use.
 *
 * @param proverb the proverb that was attempted
 * @param answer  the user's translation
 * @return "CORRECT", "CLOSE" or "INCORRECT"
 */
fun gradeAttempt(proverb: Proverb, answer: String): String {
    val cleanedAnswer = normalize(answer)

    // Exact match with the official meaning is always correct.
    if (cleanedAnswer == normalize(proverb.meaning)) {
        return "CORRECT"
    }

    // Otherwise count how many key meaning words appear in the answer.
    val matches = proverb.keywords.count { keyword -> cleanedAnswer.contains(keyword) }
    return when {
        matches >= proverb.keywords.size -> "CORRECT"
        matches >= 1 -> "CLOSE"
        else -> "INCORRECT"
    }
}

/**
 * Prints every attempt saved in the user's archive.
 *
 * @param archive the user's personal archive of attempts
 */
fun viewArchive(archive: List<Attempt>) {
    println(DIVIDER)
    println("MY ARCHIVE")
    println(DIVIDER)
    if (archive.isEmpty()) {
        println("Your archive is empty. Start a practice session to make your first attempt!")
    } else {
        println("You have ${archive.size} recorded attempt(s):")
        println(DIVIDER)
        for ((index, attempt) in archive.withIndex()) {
            println("${index + 1}. ${attempt.proverb.original}")
            println("   Your answer: ${attempt.userAnswer}")
            println("   Result: ${attempt.result}")
            println(DIVIDER)
        }
    }
}

/**
 * Shows a random proverb of the day with its meaning.
 *
 * @param proverbs the full collection of proverbs available
 */
fun showDailyProverb(proverbs: List<Proverb>) {
    println(DIVIDER)
    println("PROVERB OF THE DAY")
    println(DIVIDER)
    val proverb = proverbs[Random.nextInt(proverbs.size)]
    println("Original: ${proverb.original}")
    println("Meaning:  ${proverb.meaning}")
    println()
    println("Come back tomorrow for a new proverb. Try translating it yourself first!")
    println(DIVIDER)
}

/**
 * Cleans up text for comparison: lower-case, remove punctuation and
 * collapse extra spaces.
 *
 * @param text the raw text to clean
 * @return the cleaned text
 */
fun normalize(text: String): String =
    text.lowercase()
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()

/** A simple visual separator used throughout the console output. */
const val DIVIDER = "----------------------------------------"
