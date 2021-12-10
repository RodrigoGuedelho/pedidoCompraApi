package br.com.guedelho.pedidoCompraApi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.guedelho.pedidoCompraApi.models.ItemPedido;
import br.com.guedelho.pedidoCompraApi.models.Mesa;
import br.com.guedelho.pedidoCompraApi.models.Pedido;
import br.com.guedelho.pedidoCompraApi.repository.MesaRepository;
import br.com.guedelho.pedidoCompraApi.repository.PedidoRepository;
import br.com.guedelho.pedidoCompraApi.service.PedidoService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest
public class PedidoServiceTest {
	
	@MockBean
	private PedidoRepository pedidoRepository;
	@MockBean
	private MesaRepository mesaRepository;
	
	@Autowired
	private PedidoService pedidoService;
	
	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.pedidoService);
		
	}
	
	@Test
	public void deveSalvar_quandoPassadoOsValoresCorretos() throws Exception {
		
		Pedido pedido = Mockito.mock(Pedido.class);
		ItemPedido itemPedido = Mockito.mock(ItemPedido.class);
		List<ItemPedido> itens = new ArrayList<>();
		itens.add(itemPedido);
			
		Mockito.when(pedido.getItensPedido()).thenReturn(itens);
		Mesa mesa = Mockito.mock(Mesa.class);
		Mockito.when(mesa.getId()).thenReturn(1L);
		Mockito.when(pedido.getMesa()).thenReturn(mesa);
		Optional<Mesa> mesaOptional = Optional.of(mesa);
		Mockito.when(mesaRepository.findById(ArgumentMatchers.eq(pedido.getMesa().getId()))).thenReturn(mesaOptional);
		Mockito.when(pedidoRepository.findByMesaStatusAberto(ArgumentMatchers.eq(mesa.getId()))).thenReturn(new ArrayList<>());
		
		pedidoService.salvar(pedido, "");
	}
	
	@Test
	public void deveGerarExeption_quandoPassadoMesaOcupada()  {
		
		Pedido pedido = Mockito.mock(Pedido.class);
		ItemPedido itemPedido = Mockito.mock(ItemPedido.class);
		List<ItemPedido> itens = new ArrayList<>();
		itens.add(itemPedido);
			
		Mockito.when(pedido.getItensPedido()).thenReturn(itens);
		Mesa mesa = Mockito.mock(Mesa.class);
		Mockito.when(mesa.getId()).thenReturn(1L);
		Mockito.when(pedido.getMesa()).thenReturn(mesa);
		Optional<Mesa> mesaOptional = Optional.of(mesa);
		Mockito.when(mesaRepository.findById(ArgumentMatchers.eq(pedido.getMesa().getId()))).thenReturn(mesaOptional);
		
		List<Pedido> pedidos = new ArrayList<>();
		pedidos.add(pedido);
		Mockito.when(pedidoRepository.findByMesaStatusAberto(ArgumentMatchers.eq(mesa.getId()))).thenReturn(pedidos);
		
		try {
			pedidoService.salvar(pedido, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	

}
