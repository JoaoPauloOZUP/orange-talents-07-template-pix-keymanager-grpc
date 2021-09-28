package br.com.zupacademy.joao.grpcserver.function

import br.com.zupacademy.joao.PixRequest
import br.com.zupacademy.joao.RemovePixRequest
import br.com.zupacademy.joao.TipoChavePix.DESCONHECIDO
import br.com.zupacademy.joao.TipoContaPix.DESCONHECIDA
import br.com.zupacademy.joao.grpcserver.pix.dto.ChavePixRemoveRequest
import br.com.zupacademy.joao.grpcserver.pix.util.TipoChave
import br.com.zupacademy.joao.grpcserver.pix.util.TipoConta
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

fun RemovePixRequest.toModel(): ChavePixRemoveRequest {
    return ChavePixRemoveRequest(chavePix, clienteId)
}