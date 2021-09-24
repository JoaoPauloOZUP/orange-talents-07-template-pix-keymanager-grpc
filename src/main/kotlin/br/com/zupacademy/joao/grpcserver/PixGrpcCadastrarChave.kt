package br.com.zupacademy.joao.grpcserver

import br.com.zupacademy.joao.*
import br.com.zupacademy.joao.grpcserver.exception.ChaveExisteException
import br.com.zupacademy.joao.grpcserver.function.statusRuntimeException
import br.com.zupacademy.joao.grpcserver.function.statusRuntimeExceptionConstraint
import br.com.zupacademy.joao.grpcserver.function.toModel
import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.service.GeradorDePixService
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.validation.ConstraintViolationException

@Singleton
class PixGrpcCadastrarChave(
    private val geradorDePixService: GeradorDePixService
) : PixServiceGrpc.PixServiceImplBase() {

    private val logger = LoggerFactory.getLogger(PixGrpcCadastrarChave::class.java)

    override fun cadastrar(request: PixRequest?, responseObserver: StreamObserver<PixResponse>?) {
        logger.info("Iniciando validacoes para ${request?.cliente?.id}")

        val novaChave = request?.toModel()
        val chaveGerada = try {
            geradorDePixService.cadastrar(novaChave!!)
        } catch (e: ChaveExisteException) {
            logger.error("Chaves já existe ${novaChave?.chave}")

            statusRuntimeException(e.message, Status.ALREADY_EXISTS)
                .run { responseObserver?.onError(this) }
        } catch (e: IllegalStateException) {
            logger.error("Cliente não encontrado ${novaChave?.clienteId}")

            statusRuntimeException(e.message!!, Status.INVALID_ARGUMENT)
                .run { responseObserver?.onError(this) }
        } catch (e: ConstraintViolationException) {
            logger.error("Erro de validacoes")
            logger.error(e.message)

            statusRuntimeExceptionConstraint(e)
                .run { responseObserver?.onError(this) }
        } catch (e: Exception) {
            logger.error("Erro inesperado")
            logger.error(e.message)

            statusRuntimeException("Erro inesperado", Status.INTERNAL)
                .run { responseObserver?.onError(this) }
        } as ChavePix

        with(chaveGerada) {
            val response = PixResponse.newBuilder()
                .setCliente(
                    Cliente.newBuilder()
                        .setId(conta.titular.clientId)
                        .build()
                )
                .setPixId(chavePix)
                .build()

            responseObserver?.onNext(response)
        }

        logger.info("Chave Pix gerada ${chaveGerada.chavePix}")
        responseObserver?.onCompleted()
    }
}