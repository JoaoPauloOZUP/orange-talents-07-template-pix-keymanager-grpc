package br.com.zupacademy.joao.grpcserver.pix.service

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.dto.ChavePixRemoveRequest
import br.com.zupacademy.joao.grpcserver.repository.ChavePixRepository
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import java.lang.IllegalStateException
import javax.validation.Valid

@Validated
@Singleton
class RemovedorChavePix(
    val repository: ChavePixRepository,
) {

    fun remover(@Valid request: ChavePixRemoveRequest): ChavePix {
        with(request) {
            if(repository.existsByChavePixAndClientId(chavePix, clientId).isEmpty) {
                throw IllegalStateException("Chave inexistente")
            }

            val pix = repository.findByChavePix(chavePix).get()
            repository.delete(pix)
            return pix
        }
    }
}