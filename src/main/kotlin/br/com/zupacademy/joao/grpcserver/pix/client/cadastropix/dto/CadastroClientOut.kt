package br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto

import br.com.zupacademy.joao.grpcserver.model.Cliente

class CadastroClientOut(
    val type: String,

    val name: String,

    val taxIdNumber: String
) {

    constructor(client: Cliente) : this(
        type = "NATURAL_PERSON",
        name = client.nomeTitular,
        taxIdNumber = client.clientId
    )
}
