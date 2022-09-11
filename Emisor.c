//
// Created by tomas1646 on 30/08/22.
//
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>
#include <malloc.h>

#define HEAD_LENGTH 7
#define DATA_LENGTH 10
#define TAIL_LENGTH 3
#define PORT "/dev/pts/"

struct package {
    char head[HEAD_LENGTH];
    char data[DATA_LENGTH];
    char tail[TAIL_LENGTH];
};

//Print package fields
void printpackage(struct package package) {
    printf("Head: %s\n", package.head);
    printf("Data: %s\n", package.data);
    printf("Tail: %s\n", package.tail);
}

char *packageToString(struct package test) {
    char *x = malloc((HEAD_LENGTH + DATA_LENGTH + TAIL_LENGTH) * sizeof(char));

    strcpy(x, test.head);
    strncat(x, test.data, DATA_LENGTH - 1);
    strncat(x, test.tail, TAIL_LENGTH - 1);

    return x;
}

// FIll the package data field with the message
void fillData(struct package *test, char msg[]) {
    char data[DATA_LENGTH];

    memset(data, '0', (DATA_LENGTH - 1));

    for (int i = 0; i < strlen(msg); i++) {
        data[(DATA_LENGTH - 2) - i] = msg[i];
    }

    strcpy(test->data, data);
}

int main(int argc, char **argv) {
    char port_path[] = PORT;
    strcat(port_path, argv[1]);

    struct package test;
    memset(test.head, 'H', HEAD_LENGTH - 1);
    memset(test.tail, 'T', TAIL_LENGTH - 1);

    int fd = open(port_path, O_RDWR);

    if (fd < 0) {
        printf("Error %i from open: %s\n", errno, strerror(errno));
        return 0;
    }

    printf("*********************\n");
    printf("       Emisor\n");
    printf("*********************\n");

    while (1) {
        char msg[DATA_LENGTH] = "";

        printf("Mensaje a enviar: \n");
        scanf("%s", msg);

        fillData(&test, msg);

        char *packageEnvio = packageToString(test);

        write(fd, packageEnvio, strlen(packageEnvio));
        printf("Mensaje Enviado \n--------------\n\n");
    }
}