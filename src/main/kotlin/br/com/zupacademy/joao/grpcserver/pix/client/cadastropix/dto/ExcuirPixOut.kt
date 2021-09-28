package br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto

import br.com.zupacademy.joao.grpcserver.model.ChavePix

class ExcuirPixOut(
    val key: String,

    val participant: String
) {

    constructor(chavePix: ChavePix) : this(
        key = chavePix.chavePix,
        participant = chavePix.conta.instituicao.ispb
    )
}
