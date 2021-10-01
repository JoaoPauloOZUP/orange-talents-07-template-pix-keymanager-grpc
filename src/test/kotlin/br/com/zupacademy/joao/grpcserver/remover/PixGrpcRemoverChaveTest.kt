package br.com.zupacademy.joao.grpcserver.remover

import br.com.zupacademy.joao.PixRemoveServiceGrpc
import br.com.zupacademy.joao.RemovePixRequest
import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.client.ClientBancoCentralBrasil
import br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto.ExcluirPixInput
import br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto.ExcluirPixOut
import br.com.zupacademy.joao.grpcserver.pix.client.client.dto.ClienteInput
import br.com.zupacademy.joao.grpcserver.pix.client.client.dto.ContaInput
import br.com.zupacademy.joao.grpcserver.pix.client.client.dto.InstituicaoInput
import br.com.zupacademy.joao.grpcserver.repository.ChavePixRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

@MicronautTest(transactional = false)
internal class PixGrpcRemoverChaveTest(
    var grpcCliente: PixRemoveServiceGrpc.PixRemoveServiceBlockingStub,
    var repository: ChavePixRepository,
    var clientBcb: ClientBancoCentralBrasil
) {

    companion object {
        val CLIENT_ID_ONE = UUID.randomUUID().toString()
        val CLIENT_ID_TWO = UUID.randomUUID().toString()
    }

    @BeforeEach
    fun setup() {
        listChavePix().forEach{ repository.save(it) }
    }

    @AfterEach
    fun afterSetup() {
        repository.deleteAll()
    }

    @Test
    fun `deve excluir a chave quando for existente`() {
        `when`(clientBcb.remover("03593304015", ExcluirPixOut(listChavePix().get(0))))
            .thenReturn(HttpResponse.ok(excluirPixInput("03593304015")))

        val request = RemovePixRequest.newBuilder()
            .setChavePix("03593304015")
            .setClienteId(CLIENT_ID_ONE)
            .build()

        val response = grpcCliente.remover(request)

        with(response) {
            assertNotNull(chavePix)
            assertNotNull(clienteId)
            assertFalse(repository.existsByChavePix(chavePix))
        }
    }

    @Test
    fun `deve lancar excecao quando chave não existir`() {
        `when`(clientBcb.remover("00099988877", ExcluirPixOut(listChavePix().get(0))))
            .thenReturn(HttpResponse.notFound())


        val request = RemovePixRequest.newBuilder()
            .setChavePix("00099988877")
            .setClienteId(CLIENT_ID_ONE)
            .build()

        val throws = assertThrows(StatusRuntimeException::class.java) {
            grpcCliente.remover(request)
        }

        with(throws) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave não encontrada", status.description)
        }
    }

    @Test
    fun `a chave so deve ser removida pelo o dono da chave`() {
        `when`(clientBcb.remover("00099988877", ExcluirPixOut(listChavePix().get(0))))
            .thenReturn(HttpResponse.status(HttpStatus.FORBIDDEN))

        val request = RemovePixRequest.newBuilder()
            .setChavePix("54767608066")
            .setClienteId(CLIENT_ID_ONE)
            .build()

        val throws = assertThrows(StatusRuntimeException::class.java) {
            grpcCliente.remover(request)
        }

        with(throws) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave não encontrada", status.description)
        }
    }

    @Factory
    class clients {

        @Singleton
        fun blokingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel ) =
            PixRemoveServiceGrpc.newBlockingStub(channel)
    }

    @MockBean(ClientBancoCentralBrasil::class)
    fun bcbClient() = Mockito.mock(ClientBancoCentralBrasil::class.java)

    fun excluirPixOut(chavePix: ChavePix) = ExcluirPixOut(chavePix)

    fun excluirPixInput(key: String) = ExcluirPixInput(key = key, participant = "60701190")

    fun listChavePix(): List<ChavePix> {
        return listOf (
            ChavePix(
                chavePix = "03593304015",
                tipoChave = "CPF",
                conta = listContaInput()[0].toConta()
            ),
            ChavePix(
                chavePix = "54767608066",
                tipoChave = "CPF",
                conta = listContaInput()[1].toConta()
            )
        )
    }

    fun listContaInput(): List<ContaInput> {
        return listOf(
            ContaInput(
                tipo = "CONTA_CORRENTE",
                instituicao =  InstituicaoInput("UNIBANCO ITAU SA", "60701190"),
                agencia = "1218",
                numero = "1090",
                titular = ClienteInput(CLIENT_ID_ONE, "03593304015", "Joao")
            ),
            ContaInput(
                tipo = "CONTA_CORRENTE",
                instituicao =  InstituicaoInput("UNIBANCO ITAU SA", "60701190"),
                agencia = "1418",
                numero = "1190",
                titular = ClienteInput(CLIENT_ID_TWO, "54767608066", "José")
            )
        )
    }
}