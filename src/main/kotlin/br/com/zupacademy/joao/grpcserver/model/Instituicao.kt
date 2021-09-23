package br.com.zupacademy.joao.grpcserver.model

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class Instituicao(
    @field:NotBlank
    @Column(nullable = false)
    private val nome: String,

    @field:NotBlank
    @Column(nullable = false)
    private val ispb: String
) {

}
