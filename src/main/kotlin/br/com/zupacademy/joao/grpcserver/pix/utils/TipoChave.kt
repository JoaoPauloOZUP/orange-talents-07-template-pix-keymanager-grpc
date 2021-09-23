package br.com.zupacademy.joao.grpcserver.pix.utils


import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class TipoChave {
    DESCONHECIDO {
        override fun valida(chave: String?): Boolean {
            return false
        }
    },
    CPF {
        override fun valida(chave: String?): Boolean {
            if(chave.isNullOrBlank()) {
                return false
            }

            return CPFValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    EMAIL {
        override fun valida(chave: String?): Boolean {
            if(chave.isNullOrBlank()) {
                return false
            }

            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    CELULAR {
        override fun valida(chave: String?): Boolean {
            if(chave.isNullOrBlank()) {
                return false
            }

            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    ALEATORIO {
        override fun valida(chave: String?): Boolean {
            return chave.isNullOrBlank()
        }
    },
    ;

    abstract fun valida(chave: String?): Boolean
}