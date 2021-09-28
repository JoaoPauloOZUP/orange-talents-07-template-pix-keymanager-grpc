package br.com.zupacademy.joao.grpcserver.pix.client.cliente.dto

import br.com.zupacademy.joao.grpcserver.model.Conta
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import javax.validation.constraints.NotBlank

@Introspected
class ContaInput(
    @field:NotBlank
    private val tipo: String,

    @field:NonNull
    private val instituicao: InstituicaoInput,

    @field:NotBlank
    private val agencia: String,

    @field:NotBlank
    private val numero: String,

    @field:NotBlank
    val titular: ClienteInput
) {

    fun toConta(): Conta {
        return Conta(
            tipo,
            instituicao.toInstituicao(),
            agencia,
            numero,
            titular.toCliente()
        )
    }
}