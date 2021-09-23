package br.com.zupacademy.joao.grpcserver.model

import org.hibernate.validator.constraints.br.CPF
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class Cliente(
    @field:NotBlank
    @Column(nullable = false)
    private val clientId: String,

    @field:NotBlank
    @Column(nullable = false)
    private val nomeTitular: String,

    @field:CPF
    @Column(nullable = false)
    private val cpf: String,
) {
}