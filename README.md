# PedidoLegal

# Introdução

Essa é uma api de pedidos de compra de restaurantes. v1.0.0

# Como configurar

## Pre-requisitos

- PostgresSQL,
- Java 8 ou 11,
- Maven;

# Procedimentos para rodar projeto
0 - Criar base de dados caso não exista uma ex.: create database pedido-compra;

1 - Ajustar configurações de banco de dados no arquivo application.properties
- spring.datasource.url= jdbc:postgresql://ipOndeEstaRodandoBanco:5432/pedido-compra?createDatabaseIfNotExist=true
- spring.datasource.username=postgres
- spring.datasource.password=admin

2 - Para rodar pelo IDE basta executar a class PedidoCompraApiApplication

3- Para gerar o build da aplicação: mvn clean install

4 - executar o build com e sem o docker: 

	1 - Sem o docker: java -jar target/pedidoCompraApi-0.0.1-SNAPSHOT.jar
	2 - Com o docker:
		
		1 - criar imagem: docker build -t pedido-legal .
		2 - Rodar imagem: docker run -p 8080:8080 pedido-legal

