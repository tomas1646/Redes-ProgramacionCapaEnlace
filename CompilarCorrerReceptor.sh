gcc -o Receptor Receptor.c

array=($(ls /dev/pts -f))

./Receptor "${array[3]}"