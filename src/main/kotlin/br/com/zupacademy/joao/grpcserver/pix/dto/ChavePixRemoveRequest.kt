package br.com.zupacademy.joao.grpcserver.pix.dto

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class ChavePixRemoveRequest(
    @field:NotBlank
    val chavePix: String,

    @field:NotBlank
    val clientId: String
) {

}
