package br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto

import br.com.zupacademy.joao.grpcserver.model.Conta
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.util.TipoContaBcb

class CadastroContaOut(
    val participant: String,

    val branch: String,

    val accountNumber: String,

    val accountType: String
) {
    constructor(conta: Conta) : this (
        participant = conta.instituicao.ispb,
        branch = conta.agencia,
        accountNumber = conta.numero,
        accountType = TipoContaBcb.valueOf(conta.tipo).converter()
    )
}
