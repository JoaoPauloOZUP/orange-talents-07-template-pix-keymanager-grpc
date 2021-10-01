package br.com.zupacademy.joao.grpcserver.pix.service

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.client.ClientBancoCentralBrasil
import br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto.ExcluirPixOut
import br.com.zupacademy.joao.grpcserver.pix.dto.ChavePixRemoveRequest
import br.com.zupacademy.joao.grpcserver.repository.ChavePixRepository
import io.micronaut.http.exceptions.HttpException
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import java.lang.IllegalStateException
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class RemovedorChavePixService(
    val repository: ChavePixRepository,
    val clientBcb: ClientBancoCentralBrasil
) {

    @Transactional
    fun remover(@Valid request: ChavePixRemoveRequest): ChavePix {
        with(request) {
            val pix = repository.findByChavePixAndClientId(chavePix, clientId)
                .orElseThrow { throw IllegalStateException("Chave não encontrada") }

            try {
                clientBcb.remover(chavePix, ExcluirPixOut(pix))
            } catch (httpException: HttpException) {
                throw IllegalStateException("Chave pix não excluida")
            }

            repository.delete(pix)
            return pix
        }
    }
}