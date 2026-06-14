val mySog = environment.getIfDefined("sog")
val myCog = environment.getIfDefined("cog")
val neighborSogs = neighboring(mySog)
val neighborCogs = neighboring(myCog)

// Calcolo delle penalità per ogni vicino
val angularPenalty = neighborCogs.map { neighborCog ->
    angularMobilityPenalty(myCog, neighborCog.value)
}
val speedPenalty = neighborSogs.map { neighborSog ->
    speedMobilityPenalty(mySog, neighborSog.value)
}

// Combinazione pesata delle penalità
val combinedMobilityPenalty = angularPenalty.alignedMapValues(speedPenalty) { cogPen, sogPen ->
    (cogPen * COG_WEIGHT) + (sogPen * SOG_WEIGHT)
}.inject(environment, "mobility-penalty")

// Applicazione della penalità alla metrica originale
val mobilityAwareMetric = timeToTransmit.alignedMapValues(combinedMobilityPenalty) { time, penalty ->
    time * (1.0 + (penalty * MOBILITY_PENALTY_MULTIPLIER))
}.inject(environment, "mobility-aware-metric")
