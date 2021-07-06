package br.com.guedelho.pedidoCompraApi.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


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

}
