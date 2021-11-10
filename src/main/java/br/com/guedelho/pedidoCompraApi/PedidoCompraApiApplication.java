package br.com.guedelho.pedidoCompraApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PedidoCompraApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PedidoCompraApiApplication.class, args);
	}

}
