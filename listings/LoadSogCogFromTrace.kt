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
        // Ricerca dell'ultimo punto rispetto al tempo di simulazione
        val point = points.lastOrNull { it.time <= time } ?: points.firstOrNull()
        
        // Aggiornamento delle molecole nel nodo (Speed e Course Over Ground)
        point?.sog?.takeUnless(Double::isNaN)?.let { set("sog", it) }
        point?.cog?.takeUnless(Double::isNaN)?.let { set("cog", it) }
    }
    
    // Metodi di supporto e data class Point omessi
}
