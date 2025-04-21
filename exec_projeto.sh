#!/bin/bash

VERMELHO=$'\e[0;31m'
VERDE=$'\e[0;32m'
FIM=$'\e[0m'
STATUS_POSTGRES="$(systemctl is-active postgresql)"

if [[ -z "${API_COMPRAS_PSQL_USUARIO}" ]]; then
  echo "${VERMELHO}Erro: Você precisa definir o usuário do banco de dados para que o projeto seja executado!${FIM}"
  echo "${VERMELHO}Erro: Utilize export e defina a variavel API_COMPRAS_PSQL_USUARIO${FIM}"
  echo "${VERMELHO}Erro: Exemplo: 'export API_COMPRAS_PSQL_USUARIO=usuario'${FIM}"
  exit 1
fi

if [[ -z "${API_COMPRAS_PSQL_SENHA}" ]]; then
  echo "${VERMELHO}Erro: Você precisa definir a senha do banco de dados para que o projeto seja executado!${FIM}"
  echo "${VERMELHO}Erro: Utilize export e defina a variavel API_COMPRAS_PSQL_SENHA${FIM}"
  echo "${VERMELHO}Erro: Exemplo: 'export API_COMPRAS_PSQL_SENHA=senha'${FIM}"
  exit 1
fi

if [ "${STATUS_POSTGRES}" = "active" ]; then
  echo "${VERMELHO}Erro: O Postgres está sendo executado na sua máquina localmente!${FIM}"
  echo "${VERMELHO}Erro: Isso pode criar conflitos durante a execução do projeto${FIM}"
  echo "${VERMELHO}Erro: Por favor, pare a execução do Postgres antes de prosseguir${FIM}"
  echo "${VERMELHO}Erro: Comando recomendado: 'sudo service postgresql stop'${FIM}"
  exit 1
fi

echo "${VERDE}[projeto_compras] Executando 'mvn clean package'${FIM}"
mvn clean package

if [ $? -ne 0 ]; then
  echo "${VERMELHO}Erro ao executar 'mvn clean package'. Encerrando o script${FIM}"
  exit 1
fi

echo "${VERDE}[projeto_compras] Executando 'docker compose up -d --build'${FIM}"
docker compose up -d --build
