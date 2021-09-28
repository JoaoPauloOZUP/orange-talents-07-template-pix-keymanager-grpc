package br.com.zupacademy.joao.grpcserver.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class ChavePix(
    chavePix: String,
    tipoChave: String,
    conta: Conta
) {
    fun atualizar(key: String) {
        chavePix = key
    }

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null

//    @NotBlank
    @Column(nullable = false, unique = true, length = 77)
    var chavePix = chavePix

    @NotBlank
    @Column(nullable = false)
    val tipoChave = tipoChave

    @Embedded
    val conta = conta
}