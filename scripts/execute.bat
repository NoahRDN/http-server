
cd ../src

dir /s /b *.java > sources.txt

javac -d ../bin  @sources.txt

cd ../bin 

java server.Launcher
