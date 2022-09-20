public class BitUtils {

    // Dado una cadena de texto, devuelve la cadena en su representacion de bits
    public static String encode(String str) {
        //System.out.println("Codificando...");
        StringBuilder stringBuilder = new StringBuilder();

        byte[] bytes = str.getBytes();

        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'));
        }
        //System.out.println(str+" - "+stringBuilder.toString()+" - "+stringBuilder.toString().length());
        //System.out.println("Retornando...");
        return stringBuilder.toString();
    }

    // Dado una cadena de bits, devuelve la cadena en su representacion de texto
    public static String decode(String bits) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bits.length(); i += 8) {
            String str = bits.substring(i, i + 8);
            stringBuilder.append((char) Integer.parseInt(str, 2));
        }

        return stringBuilder.toString();
    }

    // Convierte un numero a una cadena binaria de n bits
    public static String convertNumber(Integer numero, Integer nBits) {
        int max = Integer.parseInt("1".repeat(nBits), 2);
        if (max >= numero) {
            // Es posible representar el número con los nBits
            String numeroBase2 = Integer.toBinaryString(numero);
            return "0".repeat(nBits - numeroBase2.length()) + numeroBase2;
        } else {
            System.out.println("No es posible representar tal numero con estos bits");
            return "-1";
        }

    }
}
