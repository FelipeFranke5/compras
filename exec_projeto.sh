#!/bin/bash

echo "Executando 'mvn clean package'..."
mvn clean package

if [ $? -ne 0 ]; then
  echo "Erro ao executar 'mvn clean package'. Encerrando o script..."
  exit 1
fi

echo "Executando 'docker compose up -d'..."
docker compose up -d
