private fun speedMobilityPenalty(
    sourceSog: Double,
    targetSog: Double,
): Double = when {
    sourceSog.isNaN() || targetSog.isNaN() -> 0.0
    else -> (abs(sourceSog - targetSog) / MAX_TOLERATED_SPEED_DIFF_KNOTS).coerceIn(0.0, 1.0)
}