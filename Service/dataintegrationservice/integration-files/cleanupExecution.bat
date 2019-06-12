

for /f %i in ('dir /a:d /s /b Exec*') do rd /s /q %i
