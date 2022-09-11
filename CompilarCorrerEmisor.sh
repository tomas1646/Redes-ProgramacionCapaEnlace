javac src/Emisor.java -cp $(pwd)/lib/jSerialComm-2.9.2.jar

array=($(ls /dev/pts -f))

java -cp "lib/jSerialComm-2.9.2.jar:src" Emisor "${array[2]}"