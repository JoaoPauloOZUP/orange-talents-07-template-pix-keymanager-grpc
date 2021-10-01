package br.com.zupacademy.joao.grpcserver.function

import br.com.zupacademy.joao.*
import br.com.zupacademy.joao.TipoChavePix.DESCONHECIDO
import br.com.zupacademy.joao.TipoContaPix.DESCONHECIDA
import br.com.zupacademy.joao.grpcserver.pix.dto.ChavePixRemoveRequest
import br.com.zupacademy.joao.grpcserver.pix.util.TipoChave
import br.com.zupacademy.joao.grpcserver.pix.util.TipoConta
import br.com.zupacademy.joao.grpcserver.pix.dto.NovaChaveRequest
import javax.validation.Validator
import br.com.zupacademy.joao.ConsultaPixRequest.FiltroCase.*
import br.com.zupacademy.joao.grpcserver.pix.dto.ChavePixInfo
import br.com.zupacademy.joao.grpcserver.pix.util.Filtro
import com.google.protobuf.Timestamp
import javax.validation.ConstraintViolationException


fun PixRequest.toModel(): NovaChaveRequest {
    return NovaChaveRequest(
        clienteId = cliente.id,
        tipoChave = when(tipoChave) {
            DESCONHECIDO -> null
            else -> TipoChave.valueOf(tipoChave.name)
        },
        tipoConta = when(conta) {
            DESCONHECIDA -> null
            else -> TipoConta.valueOf(conta.name)
        },
        chave = chave
    )
}

fun RemovePixRequest.toModel(): ChavePixRemoveRequest {
    return ChavePixRemoveRequest(chavePix, clienteId)
}

fun ConsultaPixRequest.toModel(validator: Validator): Filtro {
    val filtro = when(filtroCase) {
        PIXID -> pixId.let {
            Filtro.PorPixId(it.clienteId, it.chavePix)
        }

        CHAVE -> Filtro.PorChave(chave)
        FILTRO_NOT_SET -> Filtro.Invalido()
    }

    val violations = validator.validate(filtro)
    if (violations.isNotEmpty()) {
        throw ConstraintViolationException(violations)
    }

    return filtro
}

fun toTipoConta(tipo: String): String {
    return when(tipo) {
        "CACC" -> TipoConta.CONTA_CORRENTE.name
        "SVGS" -> TipoConta.CONTA_POUPANCA.name
        else -> TipoConta.valueOf(tipo).name
    }
}

fun toTipoChave(tipo: String): String {
    return when(tipo) {
        "PHONE" -> TipoChave.CELULAR.name
        "RANDOM" -> TipoChave.ALEATORIO.name
        "DOCUMENT" -> TipoChave.CPF.name
        "EMAIL" -> TipoChave.EMAIL.name
        else -> TipoChave.valueOf(tipo).name
    }
}

fun consultaPixResponse(info: ChavePixInfo): ConsultaPixResponse {
     return ConsultaPixResponse.newBuilder()
         .setChavePix(info.chavePix?:"")
         .setTipo(TipoChavePix.valueOf(toTipoChave(info.tipoChave)))
         .setClienteId(info.clienteId?:"")
         .setNomeTitular(info.conta.titular.nomeTitular)
         .setConta(Conta.newBuilder()
             .setTipo(TipoContaPix.valueOf(toTipoConta(info.tipoConta)))
             .setAgencia(info.conta.agencia)
             .setNumero(info.conta.numero)
             .build())
         .setInstituicao(Instituicao.newBuilder()
             .setIspb(info.conta.instituicao.ispb)
             .setNome(info.conta.instituicao.nome)
             .build())
         .setCriadaEm(Timestamp.newBuilder()
             .setNanos(info.registradaEm.nano)
             .setSeconds(info.registradaEm.second.toLong()))
         .build()
}