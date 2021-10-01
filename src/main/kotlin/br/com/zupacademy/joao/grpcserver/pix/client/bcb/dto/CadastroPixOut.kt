package br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.client.bcb.util.TipoChaveBcb

class CadastroPixOut(
    val keyType: String,

    val key: String,

    val bankAccount: CadastroContaOut,

    val owner: CadastroPixClientOut
) {

    constructor(chavePix: ChavePix) : this(
        keyType = TipoChaveBcb.valueOf(chavePix.tipoChave).converter(),
        key = chavePix.chavePix,
        bankAccount = CadastroContaOut(chavePix.conta),
        owner = CadastroPixClientOut(chavePix.conta.titular)
    )
}