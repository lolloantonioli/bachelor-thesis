private const val COG_WEIGHT = 0.6
private const val SOG_WEIGHT = 0.4
private const val MAX_TOLERATED_SPEED_DIFF_KNOTS = 25.0
private const val MOBILITY_PENALTY_MULTIPLIER = 2.0
private const val HALF_TURN_DEGREES = 180.0
private const val FULL_TURN_DEGREES = 360.0

/** Reads an optional Double property from the environment. */
private fun CollektiveDevice<*>.getIfDefined(name: String): Double =
    if (isDefined(name)) get<Double>(name) else Double.NaN
