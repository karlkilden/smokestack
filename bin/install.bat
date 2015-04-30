cd..
if not exist "target\conf" mkdir target\conf
copy conf target\conf /E
cd bin
call run.bat
