## Corres este script desde la terminal con el comando:
## "bash CorrerPrograma.sh"

javac src/BitUtils.java
javac -cp src src/Package.java

gnome-terminal  --tab -- bash -c "
  sleep 1
  bash $(pwd)/CompilarCorrerEmisor.sh
"


gnome-terminal  --tab -- bash -c "
  sleep 1
  bash $(pwd)/CompilarCorrerReceptor.sh
"

bash $(pwd)/AbrirPuertos.sh