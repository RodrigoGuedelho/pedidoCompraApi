package br.com.guedelho.pedidoCompraApi.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.guedelho.pedidoCompraApi.models.Mesa;
import br.com.guedelho.pedidoCompraApi.service.MesaService;
import br.com.guedelho.pedidoCompraApi.utils.Problema;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class MesaController {
	@Autowired
	private MesaService mesaService;
	
	@PostMapping("/mesas")
	public ResponseEntity<Object> salvar(@Valid @RequestBody Mesa mesa, @RequestHeader("Authorization") String token)  {
		try {	
			return  ResponseEntity.ok(mesaService.salvar(mesa, token));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
		
	}
	
	@GetMapping("/mesas")
	public ResponseEntity<Object> find(@RequestParam(value="numero", required=false, defaultValue ="0") int numero, 
			@RequestParam(value="id", required=false) Long id) {
		try {	
			return  ResponseEntity.ok(mesaService.find(numero, id));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
	}
	
	@PutMapping("/mesas/{id}")
	public ResponseEntity<Object> editar(@RequestBody Mesa mesa, @PathVariable Long id,
				@RequestHeader("Authorization") String token) {
		try {	
			mesa.setId(id);
			return  ResponseEntity.ok(mesaService.salvar(mesa, token));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
	} 
	
	@PutMapping("/mesas/{id}/cancelar")
	public ResponseEntity<Object> cancelar(@PathVariable Long id,
				@RequestHeader("Authorization") String token) {
		try {	
			return ResponseEntity.ok(mesaService.cancelar(id, token));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
	}
}
