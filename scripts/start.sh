cd ../src

find . -type f -name "*.java" > sources.txt

javac -d ../bin @sources.txt

cd ../bin

java server.Launcher
