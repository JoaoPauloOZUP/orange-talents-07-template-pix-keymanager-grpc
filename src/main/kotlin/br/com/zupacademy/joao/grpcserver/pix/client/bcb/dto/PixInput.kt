package br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.model.Cliente
import br.com.zupacademy.joao.grpcserver.model.Conta
import br.com.zupacademy.joao.grpcserver.model.Instituicao

class PixInput(
    val keyType: String,

    val key: String,

    val bankAccount: PixContaInput,

    val owner: PixClientInput,

    val createdAt: String
) {

    fun toModel(): ChavePix {
        return ChavePix(
            chavePix = key,
            tipoChave = keyType,
            conta = Conta(
                tipo = bankAccount.accountType,
                instituicao = Instituicao(
                  nome = "ITAU UNIBANCO S.A",
                  ispb = bankAccount.participant
                ),
                agencia = bankAccount.branch,
                numero = bankAccount.accountNumber,
                titular = Cliente(
                    clientId = owner.taxIdNumber,
                    nomeTitular = owner.name,
                    cpf = ""
                )
            )
        )
    }
}