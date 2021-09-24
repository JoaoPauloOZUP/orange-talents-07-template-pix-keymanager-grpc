package br.com.zupacademy.joao.grpcserver.repository

import br.com.zupacademy.joao.grpcserver.model.ChavePix
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, Long> {
    fun existsByChavePix(chave: String): Boolean
    @Query("SELECT * FROM chave_pix as c WHERE c.chave_pix = :chavePix AND c.client_id = :clientId", nativeQuery = true)
    fun existsByChavePixAndClientId(chavePix: String, clientId: String): Optional<ChavePix>
    fun findByChavePix(chavePix: String): Optional<ChavePix>
}