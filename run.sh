# !/bin/basA

rm -rf */*.class
rm -rf */*/*.class
rm -rf */*/*/*.class

javac virtualMachine/Interpreter.java
java virtualMachine/Interpreter
