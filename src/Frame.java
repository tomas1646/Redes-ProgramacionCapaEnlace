import java.util.Arrays;


public class Frame {
    private static final Integer STARTFLAG_BITS_LENGTH = 8;
    private static final Integer CONTROL_BITS_LENGTH = 8;
    private static final Integer LONGITUD_BITS_LENGTH = 7;
    private static final Integer PAYLAOD_BITS_LENGTH = 96;
    private static final Integer CRC_BITS_LENGTH = 8;
    private static final Integer ENDFLAG_BITS_LENGTH = 8;
    private static final Integer SIZE_CHAR_CONVERSION = 8;
    private String startFlag;
    private String control;
    private String longitud;
    private String payload;    
    private String endFlag;


    public Frame(String stringFrame){
        this.startFlag = stringFrame.substring(0, STARTFLAG_BITS_LENGTH);
        String body = Hamming.decodeHamming(stringFrame.substring(STARTFLAG_BITS_LENGTH,STARTFLAG_BITS_LENGTH+CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH+PAYLAOD_BITS_LENGTH+CRC_BITS_LENGTH));
        this.control=body.substring(0,CONTROL_BITS_LENGTH);
        this.longitud=body.substring(CONTROL_BITS_LENGTH,CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH);
        this.payload=body.substring(CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH,CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH+PAYLAOD_BITS_LENGTH); //Integer.parseInt(this.longitud, 2)
        this.endFlag=stringFrame.substring(STARTFLAG_BITS_LENGTH+CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH+PAYLAOD_BITS_LENGTH+CRC_BITS_LENGTH, STARTFLAG_BITS_LENGTH+CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH+PAYLAOD_BITS_LENGTH+CRC_BITS_LENGTH+ENDFLAG_BITS_LENGTH);
        }

    public Frame(String packageNetwork,Integer tipo,Integer subtipo,Integer numSecuencia,Integer siguiente){
        this.startFlag = "01111110";
        this.payload=BitUtils.encode(packageNetwork);
        //System.out.println("TEST: "+String.valueOf(PAYLAOD_BITS_LENGTH-this.payload.length())+" - "+this.payload.length());
        this.payload=this.payload+"0".repeat(PAYLAOD_BITS_LENGTH-this.payload.length());
        this.longitud=BitUtils.convertNumber(SIZE_CHAR_CONVERSION*packageNetwork.length(),LONGITUD_BITS_LENGTH);
        this.endFlag= "01111110";
        this.generateControl(tipo,subtipo,numSecuencia,siguiente);
    }

    public void printFields(){
        System.out.println("\n--------------------------");
        System.out.println("StartFlag: "+startFlag+" ("+startFlag.length()+")");
        System.out.println("Control: "+control+" ("+control.length()+")");
        System.out.println("Longitud: "+longitud+" ("+longitud.length()+")");
        String payloadUsed=payload.substring(0,Integer.parseInt(this.longitud, 2));
        System.out.println("Payload: "+payload+" ("+payload.length()+") = "+payloadUsed+" ("+payloadUsed.length()+") = "+BitUtils.decode(payloadUsed));
        System.out.println("EndFlag: "+endFlag+" ("+endFlag.length()+")");
        System.out.println("--------------------------");
        }

    // Crea una cadena de la trama lista para enviar
    public String getStringFrame(){
        return startFlag+Hamming.addHammingCode(control+longitud+payload)+endFlag;
    }

    /*
    TIPO DE TRAMA:
    [0] Trama de informacion (como necesita el emisor recibir un ACK habrá sondeo, es decir, una respuesta al envio de tal trama)
    [1] Trama no numerada (como es el receptro quien hace uso de ella no espera un ACK a tal envio)
        - Tiene subtipo, sirve para especificar un ACK o NAK
    [2] Trama de supervision (Quizas no se implemente)

     */
    public void generateControl(Integer tipo,Integer subtipo,Integer numSecuencia,Integer siguiente){
        if (tipo==0){
            /* Siguiente es el numero actual de la trama que envia */
            this.control="0"+BitUtils.convertNumber(numSecuencia,3)+"1"+BitUtils.convertNumber(siguiente,3);
        }
        else if (tipo==1){
            /* Siguiente es el numero de la trama que espera recibir ahora */
            this.control="10"+BitUtils.convertNumber(subtipo,2)+"0"+BitUtils.convertNumber(siguiente,3);
        }
        else{
            /*            
            NO TENGO PENSADO IMPLEMENTARLA
            */
        }
    }

    public static Frame detectRepairDamage(String stringFrame){
        String startFlag = stringFrame.substring(0, STARTFLAG_BITS_LENGTH);
        String body = stringFrame.substring(STARTFLAG_BITS_LENGTH,STARTFLAG_BITS_LENGTH+CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH+PAYLAOD_BITS_LENGTH+CRC_BITS_LENGTH);
        String endFlag=stringFrame.substring(STARTFLAG_BITS_LENGTH+CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH+PAYLAOD_BITS_LENGTH+CRC_BITS_LENGTH, STARTFLAG_BITS_LENGTH+CONTROL_BITS_LENGTH+LONGITUD_BITS_LENGTH+PAYLAOD_BITS_LENGTH+CRC_BITS_LENGTH+ENDFLAG_BITS_LENGTH);
        if(Hamming.checkHamming(body) != 0){
           String correctedPack = Hamming.correctDetectError(body);
           if(correctedPack == "-2")
                {
                System.out.println("Han ocurrido 2 errores, NO SE PUEDE CORREGIR\n");
                return null;
                }
            else{
                System.out.println("Paquete corregido: \n");
                return new Frame(startFlag+correctedPack+endFlag);
                } 
            }
        return new Frame(stringFrame);
        }

}

