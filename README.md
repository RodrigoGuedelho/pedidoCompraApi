# PedidoLegal

# Introdução

Essa é uma api de pedidos de compra de restaurantes. v1.0.0

# Como configurar

## Pre-requisitos

- PostgresSQL,
- Java 8 ou 11,
- Maven;

# Procedimentos para rodar projeto
0 - Criar base de dados caso não exista uma ex.: 
- create database pedido-compra;
- Se o banco foi criado agora, crie um usuario: insert into usuario values(nextval('seq_usuario'), 'admin', 'admin', '$2a$10$pP1IFOoH/iFmKwU4nd.84.dFI1y985BgRJ6W3h4vfuqbUM61iSFty', 'ATIVO');


1 - mudar no arquivo aplication.propeties: 
- spring.profiles.active=prod - para desenvolvimento 
- spring.profiles.active=dev - para produção

2 - Ajustar configurações de banco de dados no arquivo application-dev.properties caso esteja rodando tudo local
- spring.datasource.url= jdbc:postgresql://ipOndeEstaRodandoBanco:5432/pedido-compra?createDatabaseIfNotExist=true
- spring.datasource.username=postgres
- spring.datasource.password=admin

2 - Para rodar pelo IDE basta executar a class PedidoCompraApiApplication

3- Para gerar o build da aplicação: mvn clean install

4 - executar o build com, sem o docker e com pm2: 

	1 - Sem o docker: java -jar target/pedidoCompraApi-0.0.1-SNAPSHOT.jar
	2 - Com o docker:
		
		1 - criar imagem: docker build -t pedido-legal .
		2 - Rodar imagem: docker run -p 8080:8080 pedido-legal
	3 - pm2
		1 - execute o comando na pasta raiz do projeto: pm2 start pm2.json

