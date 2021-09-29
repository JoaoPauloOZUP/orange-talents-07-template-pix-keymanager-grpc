package br.com.zupacademy.joao.grpcserver.pix.client

import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto.CadastroPixOut
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto.CadastroPixInput
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto.ExcluirPixInput
import br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.dto.ExcuirPixOut
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${bcb.cadastrar-pix.url}")
interface ClientBancoCentralBrasil {

    @Post(value = "/api/v1/pix/keys")
    @Produces(value = [MediaType.APPLICATION_XML])
    @Consumes(value = [MediaType.APPLICATION_XML])
    fun cadastrarPixBcb(@Body cadastroPixOut: CadastroPixOut): HttpResponse<CadastroPixInput>

    @Delete(value = "/api/v1/pix/keys/{key}")
    @Produces(value =  [MediaType.APPLICATION_XML])
    @Consumes(value = [MediaType.APPLICATION_XML])
    fun remover(@PathVariable key: String, @Body excluirPixOut: ExcuirPixOut): HttpResponse<ExcluirPixInput>
}