package Poko

data class Envio(
    val idEnvio: Int,
    val idCliente: Int,
    var status: String,
    val numeroGuia: String,
    val destino: String,
    val origen: String,
    val repartidor: String
)
