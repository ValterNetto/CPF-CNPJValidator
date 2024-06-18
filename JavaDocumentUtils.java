public class JavaDocumentUtils {
    public static final String CPF_CENSOR = "***.###.###-**";
    public static final String CNPJ_CENSOR = "**.###.###/####-**";

    public static String formatarDocumento(String document) {
        if (document.charAt(0) == '*') {
            return document;
        } else if (document.length() == 11) {
            return aplicarCensuraAoDocumento(document, CPF_CENSOR);
        } else if (document.length() == 14) {
            return aplicarCensuraAoDocumento(document, CNPJ_CENSOR);
        } else {
            return null;
        }
    }

    public static String aplicarCensuraAoDocumento(final String textoAFormatar, final String mask) {
        String formatado = "";
        int index = 0;
        for (char m : mask.toCharArray()) {
            if (m == '*') {
                formatado += m;
                index++;
            } else if (m == '.' || m == '-' || m == '/'){
                formatado += m;
            } else {
                formatado += textoAFormatar.charAt(index);
                index++;

            }
        }
        return formatado;
    }

    public static boolean validateCPF(String cpf) {
        String formattedCpf = cpf.replaceAll("[^0-9]", "");

        if (formattedCpf == null)
            return false;

        if(formattedCpf.length() != 11)
            return false;

        char dig10,
                dig11;
        int sm, i, r, num, peso;
        try {
            sm = 0; peso = 10; for (i=0; i<9; i++) {
                num = (int)(formattedCpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48);
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(formattedCpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);
            if ((dig10 == formattedCpf.charAt(9)) && (dig11 == formattedCpf.charAt(10)))
                return(true);
            else
                return(false);
        } catch (Exception e) {
            return(false);
        }
    }

    public static boolean validateCNPJ(String cnpj) {
        String formattedCnpj = cnpj.replaceAll("[^0-9]", "");

        if (formattedCnpj.length() != 14) {
            return false;
        }

        int soma = 0;
        int peso = 2;
        for (int i = 11; i >= 0; i--) {
            soma += Character.getNumericValue(formattedCnpj.charAt(i)) * peso;
            if (peso < 9) {
                peso++;
            } else {
                peso = 2;
            }
        }
        int digitoVerificador1 = soma % 11 < 2 ? 0 : 11 - soma % 11;

        soma = 0;
        peso = 2;
        for (int i = 12; i >= 0; i--) {
            soma += Character.getNumericValue(formattedCnpj.charAt(i)) * peso;
            if (peso < 9) {
                peso++;
            } else {
                peso = 2;
            }
        }
        int digitoVerificador2 = soma % 11 < 2 ? 0 : 11 - soma % 11;

        return Character.getNumericValue(formattedCnpj.charAt(12)) == digitoVerificador1 &&
                Character.getNumericValue(formattedCnpj.charAt(13)) == digitoVerificador2;
    }
}
