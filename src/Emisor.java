import com.fazecast.jSerialComm.SerialPort;

import java.util.Scanner;

public class Emisor {
    private static final String PORT_PATH = "/dev/pts/";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String stringFrame;
        Integer actual;
        Integer numSecuencia;

        SerialPort serialPort = SerialPort.getCommPort(PORT_PATH + args[0]);
        serialPort.openPort();

        System.out.println("*********************");
        System.out.println("       Emisor");
        System.out.println("*********************");

        while (true) {
            System.out.print("Mensaje A Enviar: ");
            String str = sc.nextLine();

            numSecuencia = 0;
            actual = 0;
            Frame frame = new Frame(str, 0, null, numSecuencia, actual);
            frame.printFields();

            stringFrame = frame.getStringFrame();
            System.out.println("Trama Original: " + stringFrame);

            stringFrame = generateDamage(stringFrame);

            serialPort.writeBytes(stringFrame.getBytes(), stringFrame.getBytes().length);
            System.out.println("\n\n ---> Trama enviada <--- \n\n");
        }
    }

    public static String generateDamage(String stringFrame) {
        Scanner sc = new Scanner(System.in);
        Character agregar_error;
        Integer pos_error;
        while (true) {
            System.out.println("¿Desea agregar un error?(s/n): ");
            agregar_error = sc.next().charAt(0);
            ;
            if (agregar_error.equals('s')) {
                System.out.println("Ingrese la posicion donde quiere aragar el error (valor entero): ");
                pos_error = sc.nextInt();
                stringFrame = Hamming.addError(stringFrame, pos_error);
                System.out.println("Trama Dañada: " + stringFrame);
            } else {
                break;
            }
        }
        return stringFrame;
    }

    public static void waitResponse() {
        /* Espera el ACK o en el peor de los casos el NAK */
    }
}