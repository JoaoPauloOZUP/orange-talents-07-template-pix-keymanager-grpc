package br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.util.TipoChaveBcb

class CadastroPixOut(
    val keyType: String,

    val key: String,

    val bankAccount: CadastroContaOut,

    val owner: CadastroClientOut
) {

    constructor(chavePix: ChavePix) : this(
        keyType = TipoChaveBcb.valueOf(chavePix.tipoChave).converter(),
        key = chavePix.chavePix,
        bankAccount = CadastroContaOut(chavePix.conta),
        owner = CadastroClientOut(chavePix.conta.titular)
    )
}