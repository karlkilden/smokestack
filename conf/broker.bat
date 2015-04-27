set brokerFolder=C:\projects\WinSCP\broker2
winscp.exe /script=broker.txt /parameter %brokerFolder%
RD /S /Q %brokerFolder%
exit