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

            String codedPackage = Hamming.addHammingCode(paquete.packageToBit());                           
            Boolean ans = true;            
            while(ans){
                ans = false;
                System.out.println("¿Desea agregar un error?(s/n): ");
                char ans2 = sc.nextLine().charAt(0);               
                if(ans2 == 's'){
                    System.out.println("Inrese la posicion donde quiere aragar el error(val entero): ");
                    ans2 = sc.nextLine().charAt(0);
                    int pos = Integer.valueOf(ans2);
                    codedPackage = Hamming.addError(codedPackage, pos);          
                    ans = true;
                }                
            }
            
            serialPort.writeBytes(codedPackage.getBytes(),codedPackage.getBytes().length);
            System.out.println("Mensaje Enviado \n--------------\n\n");                            
        }        
    }
}
