import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
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

            serialPort.writeBytes(str.getBytes(), str.getBytes().length);
            System.out.println("Mensaje Enviado \n--------------\n\n");
        }
    }
}
