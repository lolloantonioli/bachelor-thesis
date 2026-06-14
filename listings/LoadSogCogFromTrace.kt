class LoadSogCogFromTrace<T>(
    private val environment: NavigationEnvironment<T>,
    node: Node<T>,
    private val path: String,
    referenceTimeEpochSeconds: Number,
) : AbstractAction<T>(node) {

    // Inizializzazione dei percorsi e filtro dei file .gpx omessi

    private val points by lazy {
        val nodeIndex = environment.nodes.indexOf(node)
        // Associazione del nodo al file traccia corrispondente
        readPoints(files[nodeIndex])
    }

    override fun execute() {
        val time = environment.simulation.time.toDouble()
        // Ricerca del punto più recente rispetto al tempo di simulazione
        val point = points.lastOrNull { it.time <= time } ?: points.firstOrNull()
        
        // Aggiornamento delle molecole nel nodo (Speed e Course Over Ground)
        point?.sog?.takeUnless(Double::isNaN)?.let { set("sog", it) }
        point?.cog?.takeUnless(Double::isNaN)?.let { set("cog", it) }
    }

    private fun readPoints(file: File): List<Point> {
        // Setup del parser XML DOM omesso
        val xmlPoints = document.getElementsByTagNameNS("*", "trkpt")
        
        return (0 until xmlPoints.length)
            .map { xmlPoints.item(it) as Element }
            .mapNotNull {
                Point(
                    // Calcolo del tempo relativo dall'inizio della simulazione
                    time = /* parsing Instant e calcolo Duration */,
                    sog = it.text("sog")?.toDoubleOrNull() ?: Double.NaN,
                    cog = it.text("cog")?.toDoubleOrNull() ?: Double.NaN,
                )
            }
            .filter { it.time >= 0.0 }
            .sortedBy { it.time }
    }
    
    // Metodi di supporto e data class Point omessi
}
