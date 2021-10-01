package br.com.zupacademy.joao.grpcserver.pix.dto

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.model.Conta
import br.com.zupacademy.joao.grpcserver.pix.util.TipoChave
import br.com.zupacademy.joao.grpcserver.pix.util.TipoConta
import java.time.LocalDateTime

data class ChavePixInfo(
    val chavePix: String? = null,
    val clienteId: String? = null,
//    val tipoChave: TipoChave,
//    val tipoConta: TipoConta,
    val tipoChave: String,
    val tipoConta: String,
    val conta: Conta,
    val registradaEm: LocalDateTime = LocalDateTime.now()
) {

    companion object {
        fun of(chave: ChavePix): ChavePixInfo {
            return ChavePixInfo(
                chavePix = chave.chavePix,
                clienteId = chave.conta.titular.clientId,
                tipoChave = chave.tipoChave,
                tipoConta = chave.conta.tipo,
                conta = chave.conta
            )
        }
    }
}