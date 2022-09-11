import com.fazecast.jSerialComm.SerialPort;

import java.util.Scanner;

public class Emisor {
    private static final String PORT_PATH = "/dev/pts/";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        SerialPort serialPort = SerialPort.getCommPort(PORT_PATH + args[0]);
        serialPort.openPort();

        System.out.println("*********************");
        System.out.println("       Emisor");
        System.out.println("*********************");

        while (true) {
            System.out.print("Mensaje A Enviar: ");
            String str = sc.nextLine();

            Package paquete = Package.createFromData(str);
            System.out.printf("Paquete: \n" + paquete + "\n");

            serialPort.writeBytes(paquete.packageToBit().getBytes(), paquete.packageToBit().getBytes().length);
            System.out.println("Mensaje Enviado \n--------------\n\n");
        }
    }
}
