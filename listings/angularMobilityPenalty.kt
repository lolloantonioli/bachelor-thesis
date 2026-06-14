private fun angularMobilityPenalty(
    sourceCog: Double,
    targetCog: Double,
): Double = when {
    sourceCog.isNaN() || targetCog.isNaN() -> 0.0
    else -> {
        val diff = abs(sourceCog - targetCog) % FULL_TURN_DEGREES
        val angularDistance = if (diff > HALF_TURN_DEGREES) FULL_TURN_DEGREES - diff else diff
        angularDistance / HALF_TURN_DEGREES
    }
}
