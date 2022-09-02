gcc -o Emisor Emisor.c

array=($(ls /dev/pts -f))

./Emisor "${array[2]}"