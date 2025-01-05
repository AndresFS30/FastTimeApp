package Poko

data class StatusEnvio(
    val idEnvio:Int,
    val status: String,
    val motivo: String?,
    val modificacion: String?,
    val idColaborador: Int
)
