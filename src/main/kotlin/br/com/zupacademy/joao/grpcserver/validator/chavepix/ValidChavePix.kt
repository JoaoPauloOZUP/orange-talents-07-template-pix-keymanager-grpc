package br.com.zupacademy.joao.grpcserver.validator.chavepix

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidChavePIxValidator::class])
annotation class ValidChavePix(
    val messsage: String = "Chave pix inválida (\${validated.tipo})",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> =[]
)


