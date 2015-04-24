cd %~dp0
winscp /script=war.txt /parameter %1 %2 %3 %4
echo Hello this a test batch file
exit