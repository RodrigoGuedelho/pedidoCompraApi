package br.com.guedelho.pedidoCompraApi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import br.com.guedelho.pedidoCompraApi.service.PedidoService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@WebMvcTest
public class PedidoControllerTest {
	
	/*@Autowired
	private PedidoController pedidoController;
	@MockBean
	private PedidoService pedidoService;
	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.pedidoController);
		
	}
	
	@Test
	public void deveRetornarBadRequeste_quandoTentarSalvarPedidoComMesaOcupada() {
		
		RestAssuredMockMvc.when(this.pedidoService.salvar(pedido, token))
		
		RestAssuredMockMvc.given()
			.accept(ContentType.JSON)
		.when()
			.post("/api/pedidos")
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}*/

}
