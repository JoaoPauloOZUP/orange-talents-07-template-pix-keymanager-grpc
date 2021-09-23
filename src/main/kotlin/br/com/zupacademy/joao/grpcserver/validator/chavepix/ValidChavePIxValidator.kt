package br.com.zupacademy.joao.grpcserver.validator.chavepix

import br.com.zupacademy.joao.grpcserver.pix.dto.NovaChaveRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

@Singleton
class ValidChavePIxValidator : ConstraintValidator<ValidChavePix, NovaChaveRequest> {
    override fun isValid(
        value: NovaChaveRequest?,
        annotationMetadata: AnnotationValue<ValidChavePix>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value?.tipoChave == null) {
            return false
        }

        return value.tipoChave.valida(value.chave)
    }
}