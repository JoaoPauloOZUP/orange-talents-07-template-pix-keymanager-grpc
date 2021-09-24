package br.com.zupacademy.joao.grpcserver

import br.com.zupacademy.joao.PixRemoveServiceGrpc
import br.com.zupacademy.joao.RemovePixRequest
import br.com.zupacademy.joao.RemovePixResponse
import br.com.zupacademy.joao.grpcserver.function.statusRuntimeException
import br.com.zupacademy.joao.grpcserver.function.toModel
import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.service.RemovedorChavePix
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException

@Singleton
class PixGrpcRemoverChave(
    private val removedorChavePix: RemovedorChavePix
) : PixRemoveServiceGrpc.PixRemoveServiceImplBase() {

    private val logger = LoggerFactory.getLogger(PixGrpcRemoverChave::class.java)

    override fun remover(request: RemovePixRequest?, responseObserver: StreamObserver<RemovePixResponse>?) {
        logger.info("Iniciando remoção chave pix ${request?.chavePix}")

        val removeChave = request?.toModel()
        val remocao = try {
            removedorChavePix.remover(removeChave!!)
        } catch (e: IllegalStateException) {
            logger.error(e.message)

            statusRuntimeException("Chave pix não encontrada", Status.INVALID_ARGUMENT)
                .run { responseObserver?.onError(this) }
        } catch (e: Exception) {
            logger.error("Erro inesperado")
            logger.error(e.message)

            statusRuntimeException("Erro inesperado", Status.INTERNAL)
                .run { responseObserver?.onError(this) }
        } as ChavePix

        with(remocao) {
            val response = br.com.zupacademy.joao.RemovePixResponse.newBuilder()
                .setChavePix(chavePix)
                .setClienteId(conta.titular.clientId)
                .build()

            logger.info("Chave pix removida $chavePix")
            responseObserver?.onNext(response)
        }

        responseObserver?.onCompleted()
    }
}