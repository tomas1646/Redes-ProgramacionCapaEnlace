public class Package {
    private static final Integer HEADER_BYTE_LENGTH = 3;
    private static final Integer DATA_BYTE_LENGTH = 20;
    private static final Integer CRC_BYTE_LENGTH = 1;

    public String header;
    public String data;
    public String crc;

    public static Package createFromData(String data){
        Package paquete = new Package();

        paquete.header = "H".repeat(HEADER_BYTE_LENGTH);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("D".repeat(DATA_BYTE_LENGTH));
        stringBuilder.replace(0, data.length(), data);        

        
        paquete.data = stringBuilder.toString();

        paquete.crc = "C".repeat(CRC_BYTE_LENGTH);

        return paquete;
    }

    public static Package createFromBitRepresentation(String str){
        Package paquete = new Package();

        paquete.header = BitUtils.getStringFromBits(str.substring(0, HEADER_BYTE_LENGTH * 8));
        paquete.data = BitUtils.getStringFromBits(str.substring(HEADER_BYTE_LENGTH * 8, (HEADER_BYTE_LENGTH + DATA_BYTE_LENGTH) * 8));
        paquete.crc = BitUtils.getStringFromBits(str.substring((HEADER_BYTE_LENGTH + DATA_BYTE_LENGTH) * 8));

        return paquete;
    }

    public String toString(){
        return "Header: " + header + "\nData: " + data + "\nCRC: " + crc;
    }

    public String packageToBit(){
        StringBuilder sb = new StringBuilder();
        sb.append(BitUtils.getBitsFromString(header));        
        sb.append(BitUtils.getBitsFromString(data));
        sb.append(BitUtils.getBitsFromString(crc));
        return sb.toString();
    }    
}
