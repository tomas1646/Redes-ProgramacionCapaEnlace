import java.util.Arrays;


public class Frame {
    private static final Integer STARTFLAG_BITS = 8;
    private static final Integer CONTROL_BITS = 2;
    private static final Integer LENGTH_BITS = 7;
    private static final Integer PAYLOAD_BITS = 96;
    private static final Integer CRC_BITS = 8;
    private static final Integer ENDFLAG_BITS = 8;
    private static final Integer SIZE_CHAR_CONVERSION = 8;
    private String startFlag;
    private String control;
    private String longitud;
    private String payload;
    private String endFlag;


    public Frame(String stringFrame) {
        this.startFlag = stringFrame.substring(0, STARTFLAG_BITS);
        String body = Hamming.decodeHamming(stringFrame.substring(STARTFLAG_BITS, STARTFLAG_BITS + CONTROL_BITS + LENGTH_BITS + PAYLOAD_BITS + CRC_BITS));
        this.control = body.substring(0, CONTROL_BITS);
        this.longitud = body.substring(CONTROL_BITS, CONTROL_BITS + LENGTH_BITS);
        this.payload = body.substring(CONTROL_BITS + LENGTH_BITS, CONTROL_BITS + LENGTH_BITS + PAYLOAD_BITS);
        this.endFlag = stringFrame.substring(STARTFLAG_BITS + CONTROL_BITS + LENGTH_BITS + PAYLOAD_BITS + CRC_BITS, STARTFLAG_BITS + CONTROL_BITS + LENGTH_BITS + PAYLOAD_BITS + CRC_BITS + ENDFLAG_BITS);
    }

    public Frame(String packageNetwork, Integer tipo, Integer subtipo, Integer numSecuencia) {
        this.startFlag = "01111110";

        this.generateControl(tipo, subtipo, numSecuencia);

        this.payload = BitUtils.encode(packageNetwork);
        this.payload = this.payload + "0".repeat(PAYLOAD_BITS - this.payload.length());
        this.longitud = BitUtils.convertNumber(SIZE_CHAR_CONVERSION * packageNetwork.length(), LENGTH_BITS);

        this.endFlag = "01111110";
    }

    public static Frame createACKFrame() {
        return new Frame("", 1, 0, null);
    }

    public static Frame createNAKFrame() {
        return new Frame("", 1, 1, null);
    }

    public boolean isACKFrame() {
        return this.control.equals("10");
    }

    public void printFields() {
        System.out.println("--------------------------");
        System.out.println("StartFlag: " + startFlag + " (" + startFlag.length() + ")");
        System.out.println("Control: " + control + " (" + control.length() + ")");
        System.out.println("Longitud: " + longitud + " (" + longitud.length() + ")");
        String payloadUsed = payload.substring(0, Integer.parseInt(this.longitud, 2));
        System.out.println("Payload: " + payload + " (" + payload.length() + ") = " + payloadUsed + " (" + payloadUsed.length() + ") = " + BitUtils.decode(payloadUsed));
        System.out.println("EndFlag: " + endFlag + " (" + endFlag.length() + ")");
        System.out.println("--------------------------");
    }

    // Crea una cadena de la trama lista para enviar
    public String getStringFrame() {
        return startFlag + Hamming.addHammingCode(control + longitud + payload) + endFlag;
    }

    /*
        TIPO DE TRAMA:
        El primer bit indica el tipo:
        [0] Trama de informacion (DATOS, se espera respuesta)
        [1] Trama no numerada (ACK / NACK)
        El segundo bit indica la secuencia, puede ser 0/1
     */
    public void generateControl(Integer tipo, Integer subtipo, Integer numSecuencia) {
        if (tipo < 0 || tipo > 1) {
            System.out.println("Error: Tipo de trama no valido");
            return;
        }

        if (tipo == 0) {
            if (numSecuencia < 0 || numSecuencia > 1) {
                System.out.println("Error: Numero de secuencia no valido");
                return;
            }

            this.control = "0" + BitUtils.convertNumber(numSecuencia, 1);
        } else {
            // tipo == 1
            if (subtipo < 0 || subtipo > 1) {
                System.out.println("Error: Subtipo de trama no valido");
                return;
            }

            this.control = "1" + BitUtils.convertNumber(subtipo, 1);
        }
    }

    public static Frame detectRepairDamage(String stringFrame) {
        String startFlag = stringFrame.substring(0, STARTFLAG_BITS);
        String body = stringFrame.substring(STARTFLAG_BITS, STARTFLAG_BITS + CONTROL_BITS + LENGTH_BITS + PAYLOAD_BITS + CRC_BITS);
        String endFlag = stringFrame.substring(STARTFLAG_BITS + CONTROL_BITS + LENGTH_BITS + PAYLOAD_BITS + CRC_BITS, STARTFLAG_BITS + CONTROL_BITS + LENGTH_BITS + PAYLOAD_BITS + CRC_BITS + ENDFLAG_BITS);
        if (Hamming.checkHamming(body) != 0) {
            String correctedPack = Hamming.correctDetectError(body);
            if (correctedPack == "-2") {
                System.out.println("Han ocurrido 2 errores, NO SE PUEDE CORREGIR\n");
                return null;
            } else {
                System.out.println("Paquete corregido: \n");
                return new Frame(startFlag + correctedPack + endFlag);
            }
        }
        return new Frame(stringFrame);
    }

    public Integer getSequenceNumber() {
        return Integer.parseInt(this.control.substring(1, 2), 2);
    }

    public String getMessage() {
        return BitUtils.decode(this.payload.substring(0, Integer.parseInt(this.longitud, 2)));
    }
}

