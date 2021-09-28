package br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto

class CadastroPixInput(
    val keyType: String,

    val key: String,

    val bankAccount: CadastroContaInput,

    val owner: CadastroClientInput,

    val createdAt: String
) {
}