package Poko

data class Colaborador(
    val idColaborador:Int,
    val nombre:String,
    val apellidoPaterno:String,
    val apellidoMaterno:String,
    val curp:String,
    val correo:String,
    val password:String,
    val idRol: Int?,
    val rol:String?,
    val fotografia: String?,
    val noPersonal:String?,
    val numeroLicencia:String?,
    val fotoBase64: String?
)
