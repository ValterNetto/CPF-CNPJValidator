class KotlinDocumentUtils {
    companion object {
        val CPF_CENSOR = "***.###.###-**"
        val CNPJ_CENSOR = "**.###.###/####-**"

        fun formatarDocumento(document: String): String? {
            return if (document[0] == '*') {
                document
            } else if (document.length == 11) {
                aplicarCensuraAoDocumento(document, CPF_CENSOR)
            } else if (document.length == 14) {
                aplicarCensuraAoDocumento(document, CNPJ_CENSOR)
            } else {
                null
            }
        }

        fun aplicarCensuraAoDocumento(textoAFormatar: String, mask: String): String? {
            var formatado: String? = ""
            var index = 0
            for (m in mask.toCharArray()) {
                if (m == '*') {
                    formatado += m
                    index++
                } else if (m == '.' || m == '-' || m == '/') {
                    formatado += m
                } else {
                    formatado += textoAFormatar[index]
                    index++
                }
            }
            return formatado
        }

        fun validateCPF(cpf: String): Boolean {
            val formattedCpf = cpf.replace("[^0-9]".toRegex(), "") ?: return false
            if (formattedCpf.length != 11) return false
            val dig10: Char
            val dig11: Char
            var sm: Int
            var i: Int
            var r: Int
            var num: Int
            var peso: Int
            return try {
                sm = 0
                peso = 10
                i = 0
                while (i < 9) {
                    num = (formattedCpf[i].code - 48)
                    sm = sm + num * peso
                    peso = peso - 1
                    i++
                }
                r = 11 - sm % 11
                dig10 = if (r == 10 || r == 11) '0' else (r + 48).toChar()
                sm = 0
                peso = 11
                i = 0
                while (i < 10) {
                    num = (formattedCpf[i].code - 48)
                    sm = sm + num * peso
                    peso = peso - 1
                    i++
                }
                r = 11 - sm % 11
                dig11 = if (r == 10 || r == 11) '0' else (r + 48).toChar()
                if (dig10 == formattedCpf[9] && dig11 == formattedCpf[10]) true else false
            } catch (e: Exception) {
                false
            }
        }

        fun validateCNPJ(cnpj: String): Boolean {
            val formattedCnpj = cnpj.replace("[^0-9]".toRegex(), "")
            if (formattedCnpj.length != 14) {
                return false
            }
            var soma = 0
            var peso = 2
            for (i in 11 downTo 0) {
                soma += Character.getNumericValue(formattedCnpj[i]) * peso
                if (peso < 9) {
                    peso++
                } else {
                    peso = 2
                }
            }
            val digitoVerificador1 = if (soma % 11 < 2) 0 else 11 - soma % 11
            soma = 0
            peso = 2
            for (i in 12 downTo 0) {
                soma += Character.getNumericValue(formattedCnpj[i]) * peso
                if (peso < 9) {
                    peso++
                } else {
                    peso = 2
                }
            }
            val digitoVerificador2 = if (soma % 11 < 2) 0 else 11 - soma % 11
            return Character.getNumericValue(formattedCnpj[12]) == digitoVerificador1 &&
                    Character.getNumericValue(formattedCnpj[13]) == digitoVerificador2
        }
    }
}