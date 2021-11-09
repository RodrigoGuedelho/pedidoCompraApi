package br.com.guedelho.pedidoCompraApi.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.models.StatusPedido;
import br.com.guedelho.pedidoCompraApi.models.Usuario;
import br.com.guedelho.pedidoCompraApi.service.UsuarioService;
import br.com.guedelho.pedidoCompraApi.utils.Problema;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("/usuarios")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<Object> salvar(@Valid @RequestBody Usuario usuario)  {
		try {
			try {	
				usuario = usuarioService.salvar(usuario);	
				return  ResponseEntity.ok(usuario);
			} catch (Exception e) {
				Problema problema = new Problema(400, e.getMessage());
				return ResponseEntity.status(problema.getStatus()).body(problema);
			}
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(400).body(problema);
		}
	}
	
	@PutMapping("/usuarios/{id}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<Object> editar(@PathVariable Long id, @Valid @RequestBody Usuario usuario)  {
		try {
			try {	
				usuario = usuarioService.salvar(usuario);	
				return  ResponseEntity.ok(usuario);
			} catch (Exception e) {
				Problema problema = new Problema(400, e.getMessage());
				return ResponseEntity.status(problema.getStatus()).body(problema);
			}
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(400).body(problema);
		}
	}
	
	@GetMapping("/usuarios")
	public ResponseEntity<Object> find(
		@RequestParam(value="login", required = false, defaultValue = "") String login,
		@RequestParam(value="nome", required = false, defaultValue = "") String nome,
		@RequestParam(value="id", required = false, defaultValue="0") Long id, 
		@RequestParam(value="status", required = false) StatusGenerico status) {
		try {
			return ResponseEntity.ok(usuarioService.find(login, nome, id, status));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			System.out.println("e.getClass()" + e.getClass());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		} 
	}
	
	@PutMapping("/usuarios/cancelar/{id}")
	public ResponseEntity<Object> cancelar(@PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
		try {	
			return ResponseEntity.ok(usuarioService.cancelar(id));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			System.out.println("e.getClass()" + e.getClass());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		} 
	}

}
