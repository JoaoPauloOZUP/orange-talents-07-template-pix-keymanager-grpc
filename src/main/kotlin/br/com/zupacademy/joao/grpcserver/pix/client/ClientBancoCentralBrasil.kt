package br.com.zupacademy.joao.grpcserver.pix.client

import br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto.CadastroPixOut
import br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto.PixInput
import br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto.ExcluirPixInput
import br.com.zupacademy.joao.grpcserver.pix.client.bcb.dto.ExcluirPixOut
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${bcb.cadastrar-pix.url}")
interface ClientBancoCentralBrasil {

    @Post(value = "/api/v1/pix/keys")
    @Produces(value = [MediaType.APPLICATION_XML])
    @Consumes(value = [MediaType.APPLICATION_XML])
    fun cadastrarPixBcb(@Body cadastroPixOut: CadastroPixOut): HttpResponse<PixInput>

    @Delete(value = "/api/v1/pix/keys/{key}")
    @Produces(value =  [MediaType.APPLICATION_XML])
    @Consumes(value = [MediaType.APPLICATION_XML])
    fun remover(@PathVariable key: String, @Body excluirPixOut: ExcluirPixOut): HttpResponse<ExcluirPixInput>

    @Get(value = "/api/v1/pix/keys/{key}")
    fun consultar(@PathVariable key: String): HttpResponse<PixInput>
}