package br.com.zupacademy.joao.grpcserver.pix.client.cadastropix.util

enum class TipoContaBcb {
    CONTA_CORRENTE {
        override fun converter(): String {
            return "CACC"
        }
    },
    CONTA_POUPANCA {
        override fun converter(): String {
            return "SVGS"
        }
    },
    ;

    abstract fun converter(): String
}