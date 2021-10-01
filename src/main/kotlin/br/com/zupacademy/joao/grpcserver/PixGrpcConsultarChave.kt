package br.com.zupacademy.joao.grpcserver

import br.com.zupacademy.joao.ConsultaPixRequest
import br.com.zupacademy.joao.ConsultaPixResponse
import br.com.zupacademy.joao.PixConsultarServiceGrpc
import br.com.zupacademy.joao.grpcserver.function.consultaPixResponse
import br.com.zupacademy.joao.grpcserver.function.toModel
import br.com.zupacademy.joao.grpcserver.pix.client.ClientBancoCentralBrasil
import br.com.zupacademy.joao.grpcserver.repository.ChavePixRepository
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.validation.Validator

@Singleton
class PixGrpcConsultarChave(
    private val repository: ChavePixRepository,
    private val clientBcb: ClientBancoCentralBrasil,
    private val validator: Validator
) : PixConsultarServiceGrpc.PixConsultarServiceImplBase() {

    private val logger = LoggerFactory.getLogger(PixGrpcConsultarChave::class.java)

    override fun consultar(request: ConsultaPixRequest?, responseObserver: StreamObserver<ConsultaPixResponse>?) {
        val filtro = request?.toModel(validator)
        val chaveInfo = filtro?.filtra(repository, clientBcb)

        logger.info("Chave pix encontrada ${chaveInfo?.chavePix}")

        responseObserver?.onNext(consultaPixResponse(chaveInfo!!))
        responseObserver?.onCompleted()
    }
}