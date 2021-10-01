package br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto

import br.com.zupacademy.joao.grpcserver.model.Cliente

class CadastroPixClientOut(
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
