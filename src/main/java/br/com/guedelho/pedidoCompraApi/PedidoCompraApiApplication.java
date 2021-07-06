package br.com.guedelho.pedidoCompraApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PedidoCompraApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidoCompraApiApplication.class, args);
	}

}
