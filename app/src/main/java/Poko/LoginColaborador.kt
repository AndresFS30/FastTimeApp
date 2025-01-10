package Poko

data class LoginColaborador(
    val error: Boolean,
    val mensaje: String,
    val colaborador: Colaborador?
)
