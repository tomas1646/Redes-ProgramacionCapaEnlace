## Corres este script desde la terminal con el comando:
## "bash CorrerProgramaXfce4.sh"
javac src/BitUtils.java
javac -cp src src/Package.java
xfce4-terminal --tab -e 'bash -c "
  bash $(pwd)/AbrirPuertos.sh;
  bash"'

xfce4-terminal --tab -e 'bash -c "
  sleep 1;
  bash $(pwd)/CompilarCorrerEmisor.sh;
  bash"'

xfce4-terminal --tab -e 'bash -c "
  sleep 1
  bash $(pwd)/CompilarCorrerReceptor.sh;
  bash"'
