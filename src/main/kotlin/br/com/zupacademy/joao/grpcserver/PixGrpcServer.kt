package br.com.zupacademy.joao.grpcserver

import br.com.zupacademy.joao.*
import br.com.zupacademy.joao.grpcserver.exception.ChaveExisteException
import br.com.zupacademy.joao.grpcserver.function.toModel
import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.service.GeradorDePixService
import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.validation.ConstraintViolationException

@Singleton
class PixGrpcServer(
    private val geradorDePixService: GeradorDePixService
) : PixGrpc.PixImplBase() {

    private val logger = LoggerFactory.getLogger(PixGrpcServer::class.java)

    override fun send(request: PixRequest?, responseObserver: StreamObserver<PixResponse>?) {
        logger.info("Iniciando validacoes para ${request?.cliente?.id}")

        val novaChave = request?.toModel()
        try {
            val chaveGerada = geradorDePixService.cadastrar(novaChave!!)
            val response = PixResponse.newBuilder()
                .setCliente(
                    Cliente.newBuilder()
                        .setId(request.cliente?.id)
                        .build()
                )
                .setPixId(chaveGerada.chavePix)
                .build()

            responseObserver?.onNext(response)
            logger.info("Chave Pix gerada ${chaveGerada.chavePix}")
        } catch (e: ChaveExisteException) {
            logger.info("Chaves já existe ${novaChave?.chave}")

            statusRuntimeException(e.message, Status.ALREADY_EXISTS)
                .run { responseObserver?.onError(this) }
        } catch (e: IllegalStateException) {
            logger.info("Cliente não encontrado ${novaChave?.clienteId}")

            statusRuntimeException(e.message!!, Status.INVALID_ARGUMENT)
                .run { responseObserver?.onError(this) }
        } catch (e: ConstraintViolationException) {
            logger.info("Erro de validacoes")
            logger.info(e.message)

            statusRuntimeExceptionConstraint(e)
                .run { responseObserver?.onError(this) }
        } catch (e: Exception) {
            logger.info("Erro inesperado")
            logger.info(e.message)

            statusRuntimeException("Erro inesperado", Status.INTERNAL)
                .run { responseObserver?.onError(this) }
        }

        responseObserver?.onCompleted()
    }

    private fun statusRuntimeException(description: String, status: Status): StatusRuntimeException? {
        return status
            .withDescription(description)
            .asRuntimeException()
    }

    private fun statusRuntimeExceptionWithDetails(description: String, details: String, status: Status): StatusRuntimeException {
        return status
            .withDescription(description)
            .augmentDescription(details)
            .asRuntimeException()
    }

    private fun statusRuntimeExceptionConstraint(e: ConstraintViolationException): StatusException? {
        val badRequest = BadRequest.newBuilder()
            .addAllFieldViolation(e.constraintViolations.map {
                FieldViolation.newBuilder()
                    .setField(it.propertyPath.last().name)
                    .setDescription(it.message)
                    .build()
            }).build()

        val statusProto = com.google.rpc.Status.newBuilder()
            .setCode(Code.INVALID_ARGUMENT_VALUE)
            .setMessage("Dados de entrada inválidos")
            .addDetails(Any.pack(badRequest))
            .build()

        return io.grpc.protobuf.StatusProto.toStatusException(statusProto)
    }
}