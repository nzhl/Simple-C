# !/bin/basA

rm -rf */*.class
rm -rf */*/*.class
rm -rf */*/*/*.class

java virtualMachine/Interpreter.java
java virtualMachine/Interpreter
