package br.com.zupacademy.joao.grpcserver.pix.client.bcb.util

enum class TipoChaveBcb {
    CPF {
        override fun converter(): String {
            return "CPF"
        }
    },
    EMAIL {
        override fun converter(): String {
            return "EMAIL"
        }
    },
    CELULAR {
        override fun converter(): String {
            return "PHONE"
        }
    },
    ALEATORIO {
        override fun converter(): String {
            return "RANDOM"
        }
    },
    ;

    abstract fun converter(): String
}