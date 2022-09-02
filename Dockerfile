FROM openjdk:11

WORKDIR /app

COPY target/pedidoCompraApi-0.0.1-SNAPSHOT.jar /app/pedidoCompra.jar
COPY  /src/main/webapp/ /app/public/
ENTRYPOINT ["java", "-jar", "pedidoCompra.jar"]