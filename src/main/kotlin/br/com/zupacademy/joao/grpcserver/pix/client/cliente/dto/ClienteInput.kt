package br.com.zupacademy.joao.grpcserver.pix.client.cliente.dto

import br.com.zupacademy.joao.grpcserver.model.Cliente
import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.NotBlank

@Introspected
class ClienteInput(
    @field:NotBlank
    val id: String,

    @field:CPF
    private val cpf: String,

    @field:NotBlank
    val nome: String
) {
    fun toCliente(): Cliente {
        return Cliente(id,nome,cpf)
    }
}
