package br.com.zupacademy.joao.grpcserver.repository

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, Long> {
    fun existsByChavePix(chave: String): Boolean
}