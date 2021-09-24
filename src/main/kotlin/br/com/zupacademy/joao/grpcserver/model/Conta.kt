package br.com.zupacademy.joao.grpcserver.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Embeddable
class Conta(
    @field:NotBlank
    @Column(nullable = false)
    private val tipo: String,

    @field:NotNull
    private val instituicao: Instituicao,

    @field:NotBlank
    @Column(nullable = false)
    private val agencia: String,

    @field:NotBlank
    @Column(nullable = false)
    private val numero: String,

    @field:NotBlank
    val titular: Cliente
) {

}