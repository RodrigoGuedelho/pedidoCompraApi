package br.com.guedelho.pedidoCompraApi.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.guedelho.pedidoCompraApi.models.Produto;
import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.service.ProdutoService;
import br.com.guedelho.pedidoCompraApi.utils.Problema;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@PostMapping("/produtos")
	public ResponseEntity<Object> salvar(@Valid @RequestBody Produto produto, @RequestHeader("Authorization") String token)  {
		try {	
			return  ResponseEntity.ok(produtoService.salvarProduto(produto, token));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
		
	}
	
	@GetMapping("/produtos")
	@CacheEvict(value="cacheFindProdutos", allEntries = true)
	@CachePut("cacheFindProdutos")
	public ResponseEntity<Object> find(@RequestParam(value="descricao", required=false) String descricao, 
			@RequestParam(value="id", required=false) Long id, @RequestParam("status") StatusGenerico status) {
		try {	
			
			return  ResponseEntity.ok(produtoService.find(descricao, id, status));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
	}
	
	@PutMapping("/produtos/{id}")
	public ResponseEntity<Object> editar(@RequestBody Produto produto, @PathVariable Long id,
				@RequestHeader("Authorization") String token) {
		try {	
			return  ResponseEntity.ok(produtoService.editarProduto(produto, id, token));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
	}
	
	@PutMapping("/produtos/{id}/upload")
	public ResponseEntity<Object> uploadImagem(@RequestParam("file") MultipartFile file, @PathVariable Long id,
				@RequestHeader("Authorization") String token) {
		try {	
			produtoService.uploadImagem(id, file);
			return ResponseEntity.status(204).body(null);
			
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
	}
	
	@GetMapping(value = "/produtos/upload/{produtoId}", produces = "application/text")
	public ResponseEntity<Object> getUploadImagem(@PathVariable("produtoId") Long produtoId,
		 HttpServletRequest httpServletRequest
		) throws Exception {	
		String img = produtoService.findImgById(produtoId, httpServletRequest.getServletContext());
		return ResponseEntity.status(HttpStatus.OK).body(img);
	}
	
	@PutMapping("/produtos/{id}/cancelar")
	public ResponseEntity<Object> cancelar(@PathVariable Long id,
				@RequestHeader("Authorization") String token) {
		try {	
			return ResponseEntity.ok(produtoService.cancelar(id, token));
		} catch (Exception e) {
			Problema problema = new Problema(400, e.getMessage());
			return ResponseEntity.status(problema.getStatus()).body(problema);
		}
	} 
}
