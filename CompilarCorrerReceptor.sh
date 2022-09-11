javac src/Receptor.java -cp "$(pwd)/lib/jSerialComm-2.9.2.jar:src"

array=($(ls /dev/pts -f))

java -cp "lib/jSerialComm-2.9.2.jar:src/*.java:src" Receptor "${array[3]}"