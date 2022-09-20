import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;

public class Receptor {
    private static final String PORT_PATH = "/dev/pts/";

    public static void main(String[] args) {
        String stringFrame;
        Frame frame;

        SerialPort serialPort = SerialPort.getCommPort(PORT_PATH + args[0]);
        serialPort.openPort();

        System.out.println("*********************");
        System.out.println("       Receptor");
        System.out.println("*********************");

        // Va a esperar hasta que se reciba un byte para leer
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        try {
            while (true) {
                System.out.println("Esperando Lectura...");
                byte[] readBuffer = new byte[1024];
                int numRead = serialPort.readBytes(readBuffer, 1024);
                String mensaje = new String(readBuffer, 0, numRead, StandardCharsets.UTF_8);

                frame = Frame.detectRepairDamage(mensaje);
                if (frame != null) {
                    frame.printFields();
                    stringFrame = frame.getStringFrame();
                    System.out.println("Trama recibida: " + stringFrame);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendResponse() {
        /* Debe enviar un ACK o NAK */

    }

}