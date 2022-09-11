javac src/Receptor.java -cp $(pwd)/lib/jSerialComm-2.9.2.jar

array=($(ls /dev/pts -f))

java -cp "lib/jSerialComm-2.9.2.jar:src" Receptor "${array[3]}"