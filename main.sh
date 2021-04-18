clear

read -p "Would you like to compile(y, n)? " compile

if [ $compile == y ]
then
	printf "\nCompiling\n"
	javac -d bin -cp ./lib/slf4j.jar:./lib/express.jar -Xdiags:verbose src/*.java
	echo Finished compiling
	printf "Exit code: $?\n\n"
fi

read -p "Would you like to run(y, n)? " run

if [ $run == y ]
then
	cd bin
	clear
	printf "Running\n\n"
	java -cp .:../lib/express.jar:../lib/slf4j.jar Main
	cd ..
	printf "\n\nExit code: $?\n"
	echo Finished running
fi