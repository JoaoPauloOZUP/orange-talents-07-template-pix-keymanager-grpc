package br.com.zupacademy.joao.grpcserver.exception

import java.lang.RuntimeException

class ChaveExisteException(
    override val message: String
) : RuntimeException() {
}