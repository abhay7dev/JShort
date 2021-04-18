clear
printf "Running\n\n"
cd bin
java -cp .:../lib/express.jar:../lib/slf4j.jar Main
cd ..
printf "\n\nExit code: $?\n"
echo Finished running