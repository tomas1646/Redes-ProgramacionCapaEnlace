import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Emisor {
    private static final String PORT_PATH = "/dev/pts/";
    private static final Integer MAX_FORWARD_ATTEMPS = 3;
    private static Integer NEXT_SEQ_NUMBER = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String stringFrame;
        String stringDamagedFrame;

        SerialPort serialPort = SerialPort.getCommPort(PORT_PATH + args[0]);
        serialPort.openPort();

        // Cuando haga lectura, se va a bloquear por 3 segundos esperando que lleguen datos.
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 3000, 0);

        System.out.println("*********************");
        System.out.println("       Emisor");
        System.out.println("*********************");

        while (true) {
            System.out.print("Mensaje A Enviar: ");
            String str = sc.nextLine();

            Frame frame = new Frame(str, 0, null, NEXT_SEQ_NUMBER);

            stringFrame = frame.getStringFrame();

            stringDamagedFrame = generateDamage(stringFrame);

            serialPort.writeBytes(stringDamagedFrame.getBytes(), stringDamagedFrame.getBytes().length);
            System.out.println("\n ---> Trama enviada <--- \n");

            try {
                handleResponse(serialPort, stringFrame, stringDamagedFrame);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public static String generateDamage(String stringFrame) {
        Scanner sc = new Scanner(System.in);
        Character agregar_error;
        Integer pos_error;

        while (true) {
            System.out.println("¿Desea agregar un error?(s/n): ");
            agregar_error = sc.next().charAt(0);

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

    public static void handleResponse(SerialPort serialPort, String stringFrame, String stringDamagedFrame) throws RuntimeException {
        /* Espera el ACK o en el peor de los casos el NAK */
        System.out.println("Esperando respuesta...");
        int forwardNumber;
        for (forwardNumber = 0; forwardNumber < MAX_FORWARD_ATTEMPS + 1; forwardNumber++) {
            byte[] readBuffer = new byte[1024];
            int numRead = serialPort.readBytes(readBuffer, 1024);

            if (numRead <= 0) {
                System.out.println("No se recibio respuesta.\n");

                if (forwardNumber == 3) {
                    throw new RuntimeException("No se recibio respuesta. El mensaje no llego a destino.");
                }

                System.out.printf("Enviando Otra vez la trama. Intento %d de 3", forwardNumber + 1);

                serialPort.writeBytes(stringDamagedFrame.getBytes(), stringDamagedFrame.getBytes().length);
            } else {
                String mensaje = new String(readBuffer, 0, numRead, StandardCharsets.UTF_8);
                Frame frame = Frame.detectRepairDamage(mensaje);

                if (frame.isACKFrame()) {
                    System.out.println("Respuesta recibida. La trama llego bien al receptor.");
                    NEXT_SEQ_NUMBER = (NEXT_SEQ_NUMBER + 1) % 2;
                    break;
                }
                if (!frame.isACKFrame()) {
                    System.out.println("Respuesta recibida. La trama llego con errores. Se reenvia la trama sin errores");
                    serialPort.writeBytes(stringFrame.getBytes(), stringFrame.getBytes().length);
                }
            }
        }
    }
}