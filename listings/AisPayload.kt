data class AisPayload(
    val boatId: Int,
    val timestamp: Instant,
    val longitude: Double,
    val latitude: Double,
    val speedOverGround: Double?,
    val courseOverGround: Double?,
) {
    companion object {
        fun from(
            boatId: Int,
            timestamp: Instant,
            aisMessage: AisMessage,
        ): AisPayload? =
            if (
                aisMessage is IPositionMessage &&
                ParsingUtils.validateLongitude(aisMessage.pos.longitudeDouble) &&
                ParsingUtils.validateLatitude(aisMessage.pos.latitudeDouble)
            ) {
                // Se messaggio non contiene sog e cog, questi diventano null
                val vesselPositionMessage = aisMessage as? IVesselPositionMessage
                AisPayload(
                    boatId,
                    timestamp,
                    aisMessage.pos.longitudeDouble,
                    aisMessage.pos.latitudeDouble,
                    if (vesselPositionMessage?.isSogValid == true) vesselPositionMessage.sog / 10.0 else null,
                    if (vesselPositionMessage?.isCogValid == true) vesselPositionMessage.cog / 10.0 else null,
                )
            } else {
                null
            }
    }
}
