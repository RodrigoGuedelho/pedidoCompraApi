package br.com.guedelho.pedidoCompraApi.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.jdbc.Expectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.guedelho.pedidoCompraApi.models.Produto;
import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.repository.ProdutoRepository;
import br.com.guedelho.pedidoCompraApi.utils.Utils;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto salvarProduto(Produto produto, String token) {
		produto.setUsuarioCadastro(Utils.getUsuarioLogado(token));
		produto.setDataCadastro(OffsetDateTime.now());
		produto.setStatus(StatusGenerico.ATIVO);
		
		return produtoRepository.save(produto);
	}
	
	public List<Produto> find(String descricao, Long id) {
		
		if (id == null) {
			id = 0L;
		}
		
		if (descricao == null) {
			descricao = "";
		}
		return produtoRepository.find(descricao, id);
	}
	
	public Produto editarProduto(Produto produto, Long id, String token) throws Exception {
		Exception validacao = validacao(produto, id);
		
		if(validacao != null) {
			throw validacao;
		}
		
		Produto produtoAxiliar = produtoRepository.findById(id).get();
		produtoAxiliar.setDescricao(produto.getDescricao());
		produtoAxiliar.setDescricaoDetalhada(produto.getDescricaoDetalhada());
		produtoAxiliar.setPreco(produto.getPreco());
		produtoAxiliar.setUsuarioCadastro(Utils.getUsuarioLogado(token));
		return produtoRepository.save(produtoAxiliar);
	}
	
	public Exception validacao(Produto produto, Long id) {
		Produto produtoAuxiliar  = produtoRepository.findById(id).get();
		if (produtoAuxiliar == null) {
			return new Exception("Usuário invalido.");
		} else if(produtoAuxiliar.getStatus().equals(StatusGenerico.CANCELADO)) {
			return new Exception("Usuário está cancelado.");
		}
		return null;
	}
}
