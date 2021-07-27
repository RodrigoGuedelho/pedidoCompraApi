package br.com.guedelho.pedidoCompraApi.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.guedelho.pedidoCompraApi.models.Pedido;
import br.com.guedelho.pedidoCompraApi.models.StatusPedido;
import br.com.guedelho.pedidoCompraApi.requestModels.PedidoRequest;
import br.com.guedelho.pedidoCompraApi.responseModels.PedidoResponse;
import br.com.guedelho.pedidoCompraApi.service.PedidoService;
import br.com.guedelho.pedidoCompraApi.utils.Problema;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class PedidoController {
	
	@Autowired
	private PedidoService pedidoService;
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/pedidos")
	public ResponseEntity<Object> salvar(@Valid @RequestBody PedidoRequest pedidoRequest, @RequestHeader("Authorization") String token)  {
		try {	
			Pedido pedido = toModel(pedidoRequest);
			System.out.println(">>> Pedido: " + pedido.toString());
			PedidoResponse pedidoResponse = toModelResponse(pedidoService.salvar(pedido, token));
			System.out.println(">>> pedidoResponse: " + pedidoResponse.toString());
			return ResponseEntity.ok(pedidoResponse);
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		} 
	}
	
	@GetMapping("/pedidos")
	public ResponseEntity<Object> find(@RequestParam("dataInicio") 
		@DateTimeFormat(pattern ="yyyy-MM-dd") LocalDate dataInicio,
		 @RequestParam(value="dataFim") @DateTimeFormat(pattern ="yyyy-MM-dd") LocalDate dataFim, 
		@RequestParam(value="observacao", required = false, defaultValue = "") String observacao,
			@RequestParam(value="id", required = false, defaultValue="0") Long id) {
		try {	
			return ResponseEntity.ok(toCollectionModelResponse(
					pedidoService.find(dataInicio, dataFim, observacao, id)));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			System.out.println("e.getClass()" + e.getClass());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		} 
	}
	
	@PutMapping("/pedidos/{id}")
	public ResponseEntity<Object> editar(@PathVariable("id") Long id, @RequestBody Pedido pedido, @RequestHeader("Authorization") String token) {
		try {	
			return ResponseEntity.ok(pedidoService.editar(id, pedido, token));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			System.out.println("e.getClass()" + e.getClass());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		} 
	}
	
	@PutMapping("/pedidos/cancelar/{id}")
	public ResponseEntity<Object> cancelar(@PathVariable("id") Long id, @RequestBody Pedido pedido, @RequestHeader("Authorization") String token) {
		try {	
			return ResponseEntity.ok(pedidoService.mudaStatusPedido(id, StatusPedido.CANCELADO));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			System.out.println("e.getClass()" + e.getClass());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		} 
	}
	
	@PutMapping("/pedidos/finalizar/{id}")
	public ResponseEntity<Object> finalizar(@PathVariable("id") Long id, @RequestBody Pedido pedido, @RequestHeader("Authorization") String token) {
		try {	
			return ResponseEntity.ok(pedidoService.mudaStatusPedido(id, StatusPedido.CANCELADO));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			System.out.println("e.getClass()" + e.getClass());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		} 
	}
	
	private Pedido toModel(PedidoRequest pedidoRequest) {
		return modelMapper.map(pedidoRequest, Pedido.class);
	}
	
	private PedidoResponse toModelResponse(Pedido pedido) {
		return modelMapper.map(pedido, PedidoResponse.class);
	}
	
	private List<PedidoResponse> toCollectionModelResponse(List<Pedido> pedidos) {	
		List<PedidoResponse> pedidosResponse = new ArrayList<>();
		
		for (Pedido pedido : pedidos) {
			pedidosResponse.add(toModelResponse(pedido));
		}
		return pedidosResponse;
	}
}
