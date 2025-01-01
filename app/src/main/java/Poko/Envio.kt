package Poko

data class Envio(
    val idEnvio: Int,
    val idCliente: Int,
    val status: String,
    val numeroGuia: String,
    val destino: String,
    val repartidor: String
)
