package Poko

data class Envio(
    val idEnvio: Int,
    val idCliente: Int,
    var status: String,
    val numeroGuia: String,
    val destino: String,
    val origen: String,
    val cliente: String,
    val contenidos: String,
    val telefono: String,
    val correo: String,
    val repartidor: String
)
