package com.aadipanchang.map.nakshatra

/**
 * Fixed list of 28 Nakshatras with equal 12°51′ (360°/28) sidereal ecliptic arcs.
 * Ashwini begins at 0° sidereal longitude (Lahiri zodiac).
 */
object NakshatraCatalog {

    /** Arc of one Nakshatra: 12°51′ = 360°/28 degrees. */
    const val NAKSHATRA_ARC_DEGREES: Float = 360f / 28f

    val NAMES = listOf(
        "Ashwini",
        "Bharani",
        "Krittika",
        "Rohini",
        "Mrigashira",
        "Ardra",
        "Punarvasu",
        "Pushya",
        "Ashlesha",
        "Magha",
        "Purva Phalguni",
        "Uttara Phalguni",
        "Hasta",
        "Chitra",
        "Swati",
        "Vishakha",
        "Anuradha",
        "Jyeshtha",
        "Moola",
        "Purvashadha",
        "Uttarashadha",
        "Shravana",
        "Dhanishta",
        "Shatabhisha",
        "Purva Bhadrapada",
        "Uttara Bhadrapada",
        "Revati",
        "Abhijit",
    )

    val HINDI_NAMES = listOf(
        "अश्विनी",
        "भरणी",
        "कृत्तिका",
        "रोहिणी",
        "मृगशिरा",
        "आर्द्रा",
        "पुनर्वसु",
        "पुष्य",
        "आश्लेषा",
        "मघा",
        "पूर्वा फाल्गुनी",
        "उत्तरा फाल्गुनी",
        "हस्त",
        "चित्रा",
        "स्वाती",
        "विशाखा",
        "अनुराधा",
        "ज्येष्ठा",
        "मूल",
        "पूर्वाषाढ़ा",
        "उत्तराषाढ़ा",
        "श्रवण",
        "धनिष्ठा",
        "शतभिषा",
        "पूर्वा भाद्रपद",
        "उत्तरा भाद्रपद",
        "रेवती",
        "अभिजित"
    )

    val all: List<Nakshatra> = NAMES.mapIndexed { index, name ->
        val start = index * NAKSHATRA_ARC_DEGREES
        val end = if (index == NAMES.lastIndex) 360f else (index + 1) * NAKSHATRA_ARC_DEGREES
        Nakshatra(name, start, end)
    }

    val count: Int get() = all.size

    fun indexOf(nakshatra: Nakshatra): Int = all.indexOf(nakshatra)

    fun get(index: Int): Nakshatra = all[index]

    fun forSiderealLongitude(longitude: Float): Nakshatra {
        val lon = normalizeDegrees(longitude)
        val index = ((lon / NAKSHATRA_ARC_DEGREES).toInt()).coerceIn(0, count - 1)
        return all[index]
    }
}

/** Normalizes an angle to [0, 360) degrees. */
fun normalizeDegrees(degrees: Float): Float {
    var d = degrees % 360f
    if (d < 0f) d += 360f
    return d
}
