// Calcolo della distanza al ground station
val clusteredTimeToStation: Double =
    distanceTo(
        groundStation,
        metric = mobilityAwareMetric,
    ).inject(environment, "clustered-timeToStation")

// Elezione del leader di cluster
val myLeader: Int =
    boundedElection(
        strength = -clusteredTimeToStation,
        bound = streamingBitRate.timeToTransmitOneMb,
        metric = mobilityAwareMetric,
        selectBest = { c1, c2 ->
            maxOf(
                c1,
                c2,
                compareBy<Candidacy<Int, Double, Double>> { it.strength }.thenBy { it.candidate },
            )
        },
    ).inject(environment, "myLeader")

// Selezione del relay per la trasmissione
val myRelay =
    potentialRelays
        .alignedMapValues(timesToStationAround + mobilityAwareMetric) { canRelay, distance ->
            when {
                 canRelay -> distance
                 else -> POSITIVE_INFINITY
            }
        }.all
        .fold(localId to POSITIVE_INFINITY) { current, entry ->
            if (entry.value < current.distanceToLeader) entry.id to entry.value else current
        }.relayId()
        .inject(environment, "myRelay")
