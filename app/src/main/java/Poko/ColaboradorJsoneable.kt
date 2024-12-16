package Poko

data class ColaboradorJsoneable(
    val IdColaborador:Int,
    val Nombre:String,
    val ApellidoPaterno:String,
    val ApellidoMaterno:String,
    val CURP:String,
    val Correo:String,
    val Password:String,
    val IdRol: Int?,
    val Rol:String?,
    val Fotografia: String?,
    val NoPersonal:String?,
    val NumeroLicencia:String?,
    val fotoBase64: String?
)
