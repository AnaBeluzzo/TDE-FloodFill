#!/bin/bash

# Script para compilar e executar a aplicação Flood Fill

echo "=== COMPILANDO APLICAÇÃO FLOOD FILL ==="

# Criar diretório de classes se não existir
mkdir -p classes

# Compilar todos os arquivos Java
echo "Compilando arquivos Java..."
javac -d classes src/*.java src/estruturas/*.java

# Verificar se a compilação foi bem-sucedida
if [ $? -eq 0 ]; then
    echo "Compilação bem-sucedida!"
    echo
    echo "=== EXECUTANDO APLICAÇÃO ==="
    echo
    
    # Executar a aplicação
    cd classes
    java Main
    cd ..
else
    echo "Erro na compilação!"
    exit 1
fi
