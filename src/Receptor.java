import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;

public class Receptor {
    private static final String PORT_PATH = "/dev/pts/";

    // Puede ser 0 o 1
    private static Integer EXPECTED_FRAME_NUMBER = 0;
    private static final Frame ACK_FRAME = Frame.createACKFrame();
    private static final Frame NAK_FRAME = Frame.createNAKFrame();

    public static void main(String[] args) {
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

                // Read Bytes from serial port
                int numRead = serialPort.readBytes(readBuffer, 1024);
                String mensaje = new String(readBuffer, 0, numRead, StandardCharsets.UTF_8);

                System.out.printf("Trama Recibida\n");
                frame = Frame.detectRepairDamage(mensaje);

                if (frame == null) {
                    System.out.printf("No se pudo arreglar error. Se solicito el reenvio de la trama\n");
                    serialPort.writeBytes(NAK_FRAME.getStringFrame().getBytes(), NAK_FRAME.getStringFrame().getBytes().length);
                } else {
                    // La trama llego. Llego sin errores, o tenia errores y se pudo corregir.

                    if (frame.getSequenceNumber() == EXPECTED_FRAME_NUMBER) {
                        EXPECTED_FRAME_NUMBER = (EXPECTED_FRAME_NUMBER + 1) % 2;
                        System.out.println("Mensaje Recibido: " + frame.getMessage());
                    }

                    serialPort.writeBytes(ACK_FRAME.getStringFrame().getBytes(), ACK_FRAME.getStringFrame().getBytes().length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}