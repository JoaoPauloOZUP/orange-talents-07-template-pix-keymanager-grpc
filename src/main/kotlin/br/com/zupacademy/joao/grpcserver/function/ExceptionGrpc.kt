package br.com.zupacademy.joao.grpcserver.function

import br.com.zupacademy.joao.BadRequest
import br.com.zupacademy.joao.FieldViolation
import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import javax.validation.ConstraintViolationException

fun statusRuntimeException(description: String, status: Status): StatusRuntimeException? {
    return status
        .withDescription(description)
        .asRuntimeException()
}

fun statusRuntimeExceptionWithDetails(description: String, details: String, status: Status): StatusRuntimeException {
    return status
        .withDescription(description)
        .augmentDescription(details)
        .asRuntimeException()
}

fun statusRuntimeExceptionConstraint(e: ConstraintViolationException): StatusException? {
    val badRequest = BadRequest.newBuilder()
        .addAllFieldViolation(e.constraintViolations.map {
            FieldViolation.newBuilder()
                .setField(it.propertyPath.last().name)
                .setDescription(it.message)
                .build()
        }).build()

    val statusProto = com.google.rpc.Status.newBuilder()
        .setCode(Code.INVALID_ARGUMENT_VALUE)
        .setMessage("Dados de entrada inválidos")
        .addDetails(Any.pack(badRequest))
        .build()

    return io.grpc.protobuf.StatusProto.toStatusException(statusProto)
}