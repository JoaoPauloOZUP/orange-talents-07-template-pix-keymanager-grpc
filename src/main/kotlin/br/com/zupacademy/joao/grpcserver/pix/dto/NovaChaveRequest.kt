package br.com.zupacademy.joao.grpcserver.pix.dto

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.model.Conta
import br.com.zupacademy.joao.grpcserver.pix.utils.TipoChave
import br.com.zupacademy.joao.grpcserver.pix.utils.TipoConta
import br.com.zupacademy.joao.grpcserver.validator.chavepix.ValidChavePix
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidChavePix
@Introspected
class NovaChaveRequest(
    @field:NotBlank
    val clienteId: String,

    @field:NotNull
    val tipoChave: TipoChave?,

    @field:NotNull
    val tipoConta: TipoConta?,

    @field:Size(max = 77)
    val chave: String
) {
    fun toChavePix(conta: Conta): ChavePix {
        return ChavePix(
            if(tipoChave == TipoChave.ALEATORIO) UUID.randomUUID().toString() else chave,
            tipoChave!!.name,
            conta
        )
    }
}