package br.com.zupacademy.joao.grpcserver.pix.util

import br.com.zupacademy.joao.grpcserver.pix.client.ClientBancoCentralBrasil
import br.com.zupacademy.joao.grpcserver.pix.dto.ChavePixInfo
import br.com.zupacademy.joao.grpcserver.repository.ChavePixRepository
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpStatus
import java.lang.IllegalStateException
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
sealed class Filtro {

    abstract fun filtra(repository: ChavePixRepository, clientBcb: ClientBancoCentralBrasil): ChavePixInfo

    @Introspected
    class PorPixId(
        @field:NotBlank val clientId: String,
        @field:NotBlank val chavePix: String
    ) : Filtro() {
        override fun filtra(repository: ChavePixRepository, clientBcb: ClientBancoCentralBrasil): ChavePixInfo {
            val possivelChavePix = repository.findByChavePixAndClientId(chavePix, clientId)
            if(possivelChavePix.isEmpty) {
                throw IllegalStateException("Chave não encontrada")
            }

            return ChavePixInfo.of(possivelChavePix.get())
        }
    }

    @Introspected
    class PorChave(
        @field:NotBlank @Size(max = 77) val chave: String
    ) : Filtro() {
        override fun filtra(repository: ChavePixRepository, clientBcb: ClientBancoCentralBrasil): ChavePixInfo {
            val possivelChavePix = repository.findByChavePix(chave)
            if(possivelChavePix.isEmpty) {
                val response = clientBcb.consultar(chave)
                if(response.status != HttpStatus.OK) {
                    throw IllegalStateException("Chave Pix não encontrada")
                }

                return ChavePixInfo.of(response.body()!!.toModel())
            }

            return ChavePixInfo.of(possivelChavePix.get())
        }
    }

    @Introspected
    class Invalido() : Filtro() {
        override fun filtra(repository: ChavePixRepository, bcbClient: ClientBancoCentralBrasil): ChavePixInfo {
            throw IllegalArgumentException("Chave Pix inválida ou não informada")
        }
    }
}
