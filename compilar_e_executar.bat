@echo off
setlocal

REM Script para compilar e executar a aplicação Flood Fill no Windows

echo === COMPILANDO APLICACAO FLOOD FILL ===

REM Criar diretório de classes se não existir
if not exist classes mkdir classes

REM Compilar todos os arquivos Java
echo Compilando arquivos Java...
javac -d classes src\*.java src\estruturas\*.java

REM Verificar se a compilação foi bem-sucedida
if %errorlevel% equ 0 (
    echo Compilacao bem-sucedida!
    echo.
    echo === EXECUTANDO APLICACAO ===
    echo.
    
    REM Executar a aplicação
    cd classes
    java Main
    cd ..
) else (
    echo Erro na compilacao!
    pause
    exit /b 1
)

pause
