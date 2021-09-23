package br.com.zupacademy.joao.grpcserver.function

import br.com.zupacademy.joao.PixRequest
import br.com.zupacademy.joao.TipoChavePix.DESCONHECIDO
import br.com.zupacademy.joao.TipoConta.DESCONHECIDA
import br.com.zupacademy.joao.grpcserver.pix.utils.TipoChave
import br.com.zupacademy.joao.grpcserver.pix.utils.TipoConta
import br.com.zupacademy.joao.grpcserver.pix.dto.NovaChaveRequest


fun PixRequest.toModel(): NovaChaveRequest {
    return NovaChaveRequest(
        clienteId = cliente.id,
        tipoChave = when(tipoChave) {
            DESCONHECIDO -> null
            else -> TipoChave.valueOf(tipoChave.name)
        },
        tipoConta = when(conta) {
            DESCONHECIDA -> null
            else -> TipoConta.valueOf(conta.name)
        },
        chave = chave
    )
}