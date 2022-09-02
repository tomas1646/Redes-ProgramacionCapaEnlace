//
// Created by tomas1646 on 30/08/22.
//
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>

#define MAX_MSG_LENGTH 256
#define PORT "/dev/pts/"

int main(int argc, char **argv) {
    char port_path[] = PORT;
    strcat(port_path, argv[1]);

    int fd = open(port_path, O_RDWR);

    if (fd < 0) {
        printf("Error %i from open: %s\n", errno, strerror(errno));
        return 0;
    }

    printf("*********************\n");
    printf("       Receptor\n");
    printf("*********************\n");

    printf("Esperando Lectura...\n");

    while (1) {
        char read_buf[MAX_MSG_LENGTH] = "";
        int num_bytes = read(fd, read_buf, sizeof(read_buf));

        if (num_bytes < 0) {
            printf("Error reading: %s\n", strerror(errno));
            return 1;
        }

        printf("Read %i bytes. Received message: %s\n\n", num_bytes, read_buf);
    }
}