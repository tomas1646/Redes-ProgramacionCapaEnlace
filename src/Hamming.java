public class Hamming {

    public static String addHammingCode(String paquete) {

        StringBuilder dataHamming = new StringBuilder();
        dataHamming.append('n'); //el primero se reserva para hacer una check de paridad de todos los datos                
        //int mask = 0x01;


        int indexData = 0;
        int indexPosition = 1;
        while (indexData < paquete.length()) {
            if (isPowerOfTwo(indexPosition)) { //Posicion donde va un bit de redundancia
                dataHamming.append('0');  //comienza con cero pero no necesariamente sera este su valor final
            } else { //posicion donde va un bit de datos
                dataHamming.append(paquete.charAt(indexData));
                indexData++;
            }
            indexPosition++;
        }
        //aca se va a asignar los valores a los bits de hamming
        int sumXor = 0;
        for (int i = 1; i < dataHamming.length(); i++) {
            if (dataHamming.charAt(i) == '1') {
                sumXor = sumXor ^ i;  //construyo un int que tendra los valores de los valores dehammin dentro de su representacion binaria
            }
        }
        for (int i = 0; i < dataHamming.length(); i++) {
            if (isPowerOfTwo(i)) {
                if (sumXor % 2 == 1) {
                    dataHamming.setCharAt(i, '1');   //asigno los valores en las posiciones correspndientes
                }
                sumXor = sumXor >>> 1;
            }

        }

        int sum = 0; //para ver la paridad de toda la trama
        for (int j = 1; j < dataHamming.length(); j++) { //calculo el bit de paridad general (posicion 0)
            if (dataHamming.charAt(j) == '1') {
                sum++;
            }
        }
        if (sum % 2 == 0) {
            dataHamming.setCharAt(0, '0');
        } else {
            dataHamming.setCharAt(0, '1');
        }

        return dataHamming.toString();
    }

    public static String decodeHamming(String paquete) {
        StringBuilder restoredPack = new StringBuilder();
        for (int i = 1; i < paquete.length(); i++) {
            if (!isPowerOfTwo(i)) {
                restoredPack.append(paquete.charAt(i));
            }
        }
        return restoredPack.toString();
    }

    public static int checkHamming(String paquete) {
        int sum = 0;
        int sumFull = 0;
        for (int i = 1; i < paquete.length(); i++) {
            if (paquete.charAt(i) == '1') {
                sum = sum ^ i;   //hacer el XOR entre las posiciones que tiene un 1 me da un entero que ubica a el error si este ha ocurrido
                sumFull++;
            }
        }
        sumFull = paquete.charAt(0) == '1' ? sumFull + 1 : sumFull;

        if (sumFull % 2 == 0) { //paridad general par
            if (sum != 0) {
                return -2; //-2 signiica que ocurrieron 2 errores                
            } else {
                return 0;
            }
        } else {  //paridad general impar (hay un error)
            if (sum == 0) {
                return -1; //-1 significa que ocurrio un error en la posicion 0
            } else {
                return sum;
            }

        }
    }

    public static String correctDetectError(String datos) {
        StringBuilder paquete = new StringBuilder();
        paquete.append(datos);
        int check = checkHamming(datos);
        switch (check) {
            case 0:
                return paquete.toString();
            case -2:
                return "-2";
            case -1:
                if (paquete.charAt(0) == '0') {
                    paquete.setCharAt(0, '1');
                } else {
                    paquete.setCharAt(0, '0');
                }
                return paquete.toString();
            default:
                if (paquete.charAt(check) == '0') {
                    paquete.setCharAt(check, '1');
                } else {
                    paquete.setCharAt(check, '0');
                }
                return paquete.toString();
        }
    }

    public static String addError(String paquete, int position) { //añade un error en el paquete pasado como argumento en la posicion pasada como argumento
        char[] errorStr = paquete.toCharArray();
        if (errorStr[position] == '0') {
            errorStr[position] = '1';
        } else {
            errorStr[position] = '0';
        }
        return String.valueOf(errorStr);
    }


    public static boolean isPowerOfTwo(int x) {
        return (x != 0 && ((x & (x - 1)) == 0)); //el and con el numero anterior a una potencia de 2 da 0.  8 -> 1000 7-> 0111. Esto no se cumple con el 0
    }

}   
