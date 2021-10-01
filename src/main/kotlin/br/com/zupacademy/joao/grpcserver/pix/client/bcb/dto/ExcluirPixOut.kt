package br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto

import br.com.zupacademy.joao.grpcserver.model.ChavePix

class ExcluirPixOut(
    val key: String,

    val participant: String
) {

    constructor(chavePix: ChavePix) : this(
        key = chavePix.chavePix,
        participant = chavePix.conta.instituicao.ispb
    )
}
