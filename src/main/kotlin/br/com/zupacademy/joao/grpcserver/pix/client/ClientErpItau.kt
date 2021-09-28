package br.com.zupacademy.joao.grpcserver.pix.client

import br.com.zupacademy.joao.grpcserver.pix.client.cliente.dto.ContaInput
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${erp.contas.url}/api/v1/clientes")
interface ClientErpItau {

    @Get("/{clientId}/contas{?tipo}")
    fun consultaConta(@PathVariable clientId: String, @QueryValue tipo: String): ContaInput
}