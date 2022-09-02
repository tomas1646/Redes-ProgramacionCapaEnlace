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
    printf("       Emisor\n");
    printf("*********************\n");

    while (1) {
        char msg[MAX_MSG_LENGTH] = "";

        printf("Mensaje a enviar: \n");
        scanf("%s", msg);

        write(fd, msg, strlen(msg));
        printf("Mensaje Enviado \n--------------\n\n");
    }
}