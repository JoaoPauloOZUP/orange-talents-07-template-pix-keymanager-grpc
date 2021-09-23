package br.com.zupacademy.joao.grpcserver.pix.client.dto

import br.com.zupacademy.joao.grpcserver.model.Instituicao
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class InstituicaoInput(
    @field:NotBlank
    private val nome: String,

    @field:NotBlank
    private val ispb: String
) {
    fun toInstituicao(): Instituicao {
        return Instituicao(nome,ispb)
    }

}
