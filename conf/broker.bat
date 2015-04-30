set brokerFolder=C:\projects\WinSCP\broker2
winscp.exe /script=broker.txt /parameter %brokerFolder%
RD /S /Q %brokerFolder%
if not exist "%brokerFolder%" mkdir %brokerFolder%
exit