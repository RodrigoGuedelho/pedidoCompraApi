FROM openjdk

WORKDIR /app

COPY target/pedidoCompraApi-0.0.1-SNAPSHOT.jar /app/pedidoCompra.jar

ENTRYPOINT ["java", "-jar", "pedidoCompra.jar"]