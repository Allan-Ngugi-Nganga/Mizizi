package com.example.mizizi.data.local

/** The eight proverbs the console prototype shipped with, reused as seed data. */
object SeedData {

    data class Seed(val original: String, val meaning: String, val keywords: String)

    val proverbs: List<Seed> = listOf(
        Seed(
            "Haraka haraka haina baraka",
            "Haste makes waste",
            "haste, hurry, waste, blessings"
        ),
        Seed(
            "Asiyekujua hakuthamini",
            "He who does not know you does not value you",
            "know, value, appreciate"
        ),
        Seed(
            "Mvumilivu hula mbivu",
            "The patient one eats ripe fruit",
            "patient, patience, ripe, fruit"
        ),
        Seed(
            "Mtaka yote hukosa yote",
            "He who wants everything loses everything",
            "everything, wants, loses, all"
        ),
        Seed(
            "Samaki mkunje angali mbichi",
            "Bend the fish while it is still fresh",
            "bend, fresh, young, teach"
        ),
        Seed(
            "Akili nyingi huondoa maarifa",
            "Too much cunning removes wisdom",
            "cunning, wisdom, clever"
        ),
        Seed(
            "Fadhili ni rehani",
            "A favour is a loan that must be repaid",
            "favour, favor, loan, repay"
        ),
        Seed(
            "Penye nia pana njia",
            "Where there is a will, there is a way",
            "will, way, determination"
        )
    )
}
