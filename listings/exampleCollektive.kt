fun Aggregate<Int>.temperatureEntrypoint(env: EnvironmentVariables, collektiveDevice: CollektiveDevice<*>): Boolean {
    val temp: Double = env["temperature"]
    val distances = with(collektiveDevice) { distances() }
    val isLeader = (boundedElection(bound = 20.0, metric = distances) == localId)
        .also { env["leader"] = it }
    val closestLeader = gradientCast(source = isLeader, local = localId, metric = distances)
    return alignedOn(closestLeader) {
        val regDistances = with(collektiveDevice) { distances() }
        val count = countDevices(sink = isLeader)
        val sum = convergeSum(sink = isLeader, local = temp)
        val avg = if (isLeader && count > 0) sum / count else 0.0
        val alarmDecision = isLeader && avg > 30.0
        gradientCast(source = isLeader, local = alarmDecision, metric = regDistances)
            .also { env["alarm"] = it }
    }
}