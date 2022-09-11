public class BitUtils {
    // Dado una cadena de texto, devuelve la cadena en su representacion de bits
    public static String getBitsFromString(String str) {
        StringBuilder stringBuilder = new StringBuilder();

        byte[] bytes = str.getBytes();

        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'));
        }

        return stringBuilder.toString();
    }

    // Dado una cadena de bits, devuelve la cadena en su representacion de texto
    public static String getStringFromBits(String bits) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bits.length(); i += 8) {
            String str = bits.substring(i, i + 8);
            stringBuilder.append((char) Integer.parseInt(str, 2));
        }

        return stringBuilder.toString();
    }
}
