package br.com.zupacademy.joao.grpcserver.cadastrar

import br.com.zupacademy.joao.*
import br.com.zupacademy.joao.grpcserver.model.ChavePix
import br.com.zupacademy.joao.grpcserver.pix.client.ClientBancoCentralBrasil
import br.com.zupacademy.joao.grpcserver.pix.client.ClientErpItau
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto.CadastroClientInput
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto.CadastroContaInput
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto.CadastroPixInput
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto.CadastroPixOut
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.util.TipoContaBcb
import br.com.zupacademy.joao.grpcserver.pix.client.cliente.dto.ClienteInput
import br.com.zupacademy.joao.grpcserver.pix.client.cliente.dto.ContaInput
import br.com.zupacademy.joao.grpcserver.pix.client.cliente.dto.InstituicaoInput
import br.com.zupacademy.joao.grpcserver.pix.util.TipoChave
import br.com.zupacademy.joao.grpcserver.repository.ChavePixRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.Instant
import java.util.*

@MicronautTest(transactional = false)
class PixGrpcCadastrarChaveTest(
    val grpcClient: PixServiceGrpc.PixServiceBlockingStub,
    val repository: ChavePixRepository,
    val erpItau: ClientErpItau,
    val clientBcb: ClientBancoCentralBrasil
) {

    /**
     * ter um percentual de cobertura de no mínimo 90% ;
     * ter coberto cenários felizes (happy-path) e fluxos alternativos;
     * não precisar de instruções especiais para preparar o ambiente ou para rodar sua bateria de testes;
     * sua bateria de testes deve rodar tanto na sua IDE quanto via ;
     * que outro desenvolvedor(a) do time consiga rodar facilmente a bateria de testes do seu serviço;
     * */

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    companion object {
        val CLIENT_ID = UUID.randomUUID().toString()
    }

    @Test
    fun `deve criar uma nova chave pix aleatorio`() {
        `when`(erpItau.consultaConta(CLIENT_ID, "CONTA_CORRENTE"))
            .thenReturn(contaInput())

        `when`(clientBcb.cadastrarPixBcb(CadastroPixOut(chavePix())))
            .thenReturn(HttpResponse.ok(cadastroPixInput("")))

        val request = PixRequest.newBuilder()
            .setCliente(
                Cliente.newBuilder()
                    .setId(CLIENT_ID)
                    .build()
            )
            .setTipoChave(TipoChavePix.ALEATORIO)
            .setConta(TipoContaPix.CONTA_CORRENTE)
            .setChave("")
            .build()

        val response = grpcClient.cadastrar(request)

        // Validação
        with(response) {
            assertNotNull(cliente.id == CLIENT_ID)
            assertNotNull(cliente)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `deve cadastrar chave pix CPF`() {
        `when`(erpItau.consultaConta(CLIENT_ID, "CONTA_CORRENTE"))
            .thenReturn(contaInput())

        `when`(clientBcb.cadastrarPixBcb(CadastroPixOut(chavePix())))
            .thenReturn(HttpResponse.ok(cadastroPixInput("02467781054")))

        val request = PixRequest.newBuilder()
            .setCliente(
                Cliente.newBuilder()
                    .setId(CLIENT_ID)
                    .build()
            )
            .setTipoChave(TipoChavePix.CPF)
            .setConta(TipoContaPix.CONTA_CORRENTE)
            .setChave("02467781054")
            .build()

        val response = grpcClient.cadastrar(request)

        with(response) {
            assertNotNull(cliente.id == CLIENT_ID)
            assertNotNull(cliente)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `deve cadastrar chave pix email`() {
        `when`(erpItau.consultaConta(CLIENT_ID, "CONTA_CORRENTE"))
            .thenReturn(contaInput())

        `when`(clientBcb.cadastrarPixBcb(CadastroPixOut(chavePix())))
            .thenReturn(HttpResponse.ok(cadastroPixInput("mail@mail.com")))

        val request = PixRequest.newBuilder()
            .setCliente(
                Cliente.newBuilder()
                    .setId(CLIENT_ID)
                    .build()
            )
            .setTipoChave(TipoChavePix.EMAIL)
            .setConta(TipoContaPix.CONTA_CORRENTE)
            .setChave("mail@mail.com")
            .build()

        val response = grpcClient.cadastrar(request)

        with(response) {
            assertNotNull(cliente.id == CLIENT_ID)
            assertNotNull(cliente)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `deve cadastrar chave pix celular`() {
        `when`(erpItau.consultaConta(CLIENT_ID, "CONTA_CORRENTE"))
            .thenReturn(contaInput())

        `when`(clientBcb.cadastrarPixBcb(CadastroPixOut(chavePix())))
            .thenReturn(HttpResponse.ok(cadastroPixInput("+5519988496397")))

        val pixRequestChaveCelular = PixRequest.newBuilder()
            .setCliente(
                Cliente.newBuilder()
                    .setId(CLIENT_ID)
                    .build()
            )
            .setTipoChave(TipoChavePix.CELULAR)
            .setConta(TipoContaPix.CONTA_CORRENTE)
            .setChave("+5519988496397")
            .build()


        val response = grpcClient.cadastrar(pixRequestChaveCelular)

        // Validação
        with(response) {
            assertNotNull(cliente.id == CLIENT_ID)
            assertNotNull(cliente)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `nao deve cadastrar pix valores invalidos`() {
        `when`(erpItau.consultaConta(CLIENT_ID, "CONTA_CORRENTE"))
            .thenReturn(contaInput())


        val request = PixRequest.newBuilder()
            .setCliente(
                Cliente.newBuilder()
                    .setId(CLIENT_ID)
                    .build()
            )
            .setTipoChave(TipoChavePix.CPF)
            .setConta(TipoContaPix.CONTA_CORRENTE)
            .setChave("")
            .build()

        val thrown = assertThrows(StatusRuntimeException::class.java) {
            grpcClient.cadastrar(request)
        }

        with(thrown) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Dados de entrada inválidos", status.description)
        }
    }

    @Test
    fun `nao deve cadastrar pix valores chave existente`() {
        repository.save(chavePix())

        `when`(erpItau.consultaConta(CLIENT_ID, "CONTA_CORRENTE"))
            .thenReturn(contaInput())

        val request = PixRequest.newBuilder()
            .setCliente(
                Cliente.newBuilder()
                    .setId(CLIENT_ID)
                    .build()
            )
            .setTipoChave(TipoChavePix.CPF)
            .setConta(TipoContaPix.CONTA_CORRENTE)
            .setChave("02467781054")
            .build()

        val thrown = assertThrows(StatusRuntimeException::class.java) {
            grpcClient.cadastrar(request)
        }
        with(thrown) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave já existente", status.description)
        }
    }

    @Test
    fun `deve retornar false sempre que tipo da chave for diferente`() {
        assertFalse(TipoChave.ALEATORIO.valida("45914248809"))
        assertFalse(TipoChave.ALEATORIO.valida("mail@mail.com"))
        assertFalse(TipoChave.ALEATORIO.valida("+5519988496397"))

        assertFalse(TipoChave.CPF.valida("mail@mail.com"))
        assertFalse(TipoChave.CPF.valida("+5519988496397"))
        assertFalse(TipoChave.CPF.valida(""))

        assertFalse(TipoChave.EMAIL.valida("02467781054"))
        assertFalse(TipoChave.EMAIL.valida("+5519988496397"))
        assertFalse(TipoChave.EMAIL.valida(""))

        assertFalse(TipoChave.CELULAR.valida("02467781054"))
        assertFalse(TipoChave.CELULAR.valida("mail@mail.com"))
        assertFalse(TipoChave.CELULAR.valida(""))

        assertFalse(TipoChave.DESCONHECIDO.valida(""))
    }

    @Test
    fun `deve retornar false sempre que tipo da chave fo nula`() {
        val request = PixRequest.newBuilder()
            .setCliente(
                Cliente.newBuilder()
                    .setId(CLIENT_ID)
                    .build()
            )
            .setTipoChave(TipoChavePix.DESCONHECIDO)
            .setConta(TipoContaPix.CONTA_CORRENTE)
            .setChave("")
            .build()

        val thrown = assertThrows(StatusRuntimeException::class.java) {
            grpcClient.cadastrar(request)
        }

        with(thrown) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Dados de entrada inválidos", status.description)
        }
    }

    @MockBean(ClientErpItau::class)
    fun itauClient() = Mockito.mock(ClientErpItau::class.java)

    @MockBean(ClientBancoCentralBrasil::class)
    fun bcbClient() = Mockito.mock(ClientBancoCentralBrasil::class.java, Mockito.RETURNS_DEEP_STUBS)

    @Factory
    class Clients {
        // O micronaut levanta o servidor GRPC em uma porta aleatória.
        // Isso ocorre pois se tivermos uma bateria de testes grande seria possível paralelizar com um servidor de integração continua.
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) =
            PixServiceGrpc.newBlockingStub(channel)
    }

    fun contaInput(): ContaInput {
        return ContaInput(
            tipo = "CONTA_CORRENTE",
            instituicao =  InstituicaoInput("UNIBANCO ITAU SA", "60701190"),
            agencia = "0001",
            numero = "123456",
            titular = ClienteInput(CLIENT_ID, "02467781054", "Joao")
        )
    }

    fun chavePix(): ChavePix {
        return ChavePix(
            chavePix = "02467781054",
            tipoChave = "CPF",
            conta = contaInput().toConta()
        )
    }

    fun cadastroPixInput(param: String): CadastroPixInput {
        return CadastroPixInput(
            keyType = TipoContaBcb.valueOf(contaInput().tipo).converter() ,
            key = if(param == null || param.isBlank()) UUID.randomUUID().toString() else param,
            bankAccount = CadastroContaInput(
                participant = "60701190",
                branch = "0001",
                accountNumber = "123456",
                accountType = TipoContaBcb.valueOf(contaInput().tipo).converter()
            ),
            owner = CadastroClientInput(
                type = "NATURAL_PERSON",
                name = contaInput().titular.nome,
                taxIdNumber = contaInput().titular.id
            ),
            createdAt = Instant.now().toString()
        )
    }
}
