set -e

# javac *.java
javac Server.java ChatServer.java
java ChatServer $1