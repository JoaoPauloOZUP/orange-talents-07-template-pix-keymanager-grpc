package br.com.zupacademy.joao.grpcserver.pix.service

import br.com.zupacademy.joao.grpcserver.pix.client.ClientErpItau
import br.com.zupacademy.joao.grpcserver.exception.ChaveExisteException
import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.repository.ChavePixRepository
import br.com.zupacademy.joao.grpcserver.pix.dto.NovaChaveRequest
import io.micronaut.http.exceptions.HttpException
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.validation.Valid

@Validated
@Singleton
class GeradorDePixService(
    private val repository: ChavePixRepository,
    private val clientItau: ClientErpItau
) {

    private val logger = LoggerFactory.getLogger(GeradorDePixService::class.java)

    fun cadastrar(@Valid novaChaveRequest: NovaChaveRequest): ChavePix {
        if(repository.existsByChavePix(novaChaveRequest.chave)) {
            logger.info("Chave existente")
            throw ChaveExisteException("Chave já existente")
        }

        val conta = try {
            clientItau.consultaConta(novaChaveRequest.clienteId, novaChaveRequest.tipoConta!!.name).toConta()
        } catch (httpException: HttpException) {
            logger.info("Cliente não encontrado")
            throw IllegalStateException("Cliente não encontrado")
        }

        val chavePix = novaChaveRequest.toChavePix(conta)
        repository.save(chavePix)
        logger.info("Pix salvo")

        return chavePix;
    }
}