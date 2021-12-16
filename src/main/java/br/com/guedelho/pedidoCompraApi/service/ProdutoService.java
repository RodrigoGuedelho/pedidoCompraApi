package br.com.guedelho.pedidoCompraApi.service;

import java.io.File;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.guedelho.pedidoCompraApi.models.Produto;
import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.repository.ProdutoRepository;
import br.com.guedelho.pedidoCompraApi.utils.Utils;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	private String CAMINHO_IMG = Utils.CAMINHO_RAIZ_IMAGEM + "produtos/";
	
	public Produto salvarProduto(Produto produto, String token) {
		produto.setUsuarioCadastro(Utils.getUsuarioLogado(token));
		produto.setDataCadastro(OffsetDateTime.now());
		produto.setStatus(StatusGenerico.ATIVO);
		
		return produtoRepository.save(produto);
	}
	
	public List<Produto> find(String descricao, Long id, StatusGenerico status) {
		
		if (id == null) {
			id = 0L;
		}
		
		if (descricao == null) {
			descricao = "";
		}
		return produtoRepository.find(descricao, id, status);
	}
	
	public Produto editarProduto(Produto produto, Long id, String token) throws Exception {
		Exception validacao = validacao(id);
		
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
	
	
	public Produto uploadImagem(Long id, MultipartFile file) throws Exception {
		if (file.isEmpty())
			throw new Exception("Upload vazio.");
		Produto produtoAxiliar = produtoRepository.findById(id).get();
		if (produtoAxiliar == null)
			throw new Exception("Id do produto invalido.");
		
		try {
			String nomeImagem = Utils.uploadImagem(id, file, CAMINHO_IMG);	
			produtoAxiliar.setNomeImagem(nomeImagem);
			return produtoRepository.save(produtoAxiliar);	
		} catch (Exception e) {
			throw new Exception("Erro inesperado ao fazer o upload do arquivo");
		}
	}
	
	public String findImgById(Long produtoId, ServletContext servletContext) throws Exception {
		Produto produto = produtoRepository.findById(produtoId).get();	
		if (produto == null)
			throw new Exception("Id do produto invalido.");
		if (produto.getNomeImagem().isEmpty())
			throw new Exception("Produto Não possui imagem");
		
		return Utils.getImagem(CAMINHO_IMG + produto.getNomeImagem());
	}
	
	
	public Produto cancelar(Long id, String token) throws Exception {
		Exception validacao = validacao(id);
		
		if(validacao != null) {
			throw validacao;
		}
		Produto produto = produtoRepository.findById(id).get();
		produto.setStatus(StatusGenerico.CANCELADO);
		produto.setUsuarioCadastro(Utils.getUsuarioLogado(token));
		return produtoRepository.save(produto);
	}
	
	public Exception validacao(Long id) {
		Produto produtoAuxiliar  = produtoRepository.findById(id).get();
		if (produtoAuxiliar == null) {
			return new Exception("Produto invalido.");
		} else if(produtoAuxiliar.getStatus().equals(StatusGenerico.CANCELADO)) {
			return new Exception("Produto está cancelado.");
		}
		return null;
	}
}
