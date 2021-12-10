package br.com.guedelho.pedidoCompraApi.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.Image;

import br.com.guedelho.pedidoCompraApi.models.Produto;
import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.repository.ProdutoRepository;
import br.com.guedelho.pedidoCompraApi.utils.Utils;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	private String CAMINHO_IMG = "/opt/pedido-legal/uploads/produtos/";
	
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
			String[] nomeFileQuebrado = file.getOriginalFilename().split("\\.");
			System.out.println(">>>> array" + nomeFileQuebrado.length);
			String extencaoArquivo = nomeFileQuebrado[nomeFileQuebrado.length -1];
			Path path = Paths.get(CAMINHO_IMG);
			Files.createDirectories(path);
			Path pathImg = Paths.get(CAMINHO_IMG + id.toString() + "." + extencaoArquivo);
			Files.write(pathImg, file.getBytes());
			produtoAxiliar.setNomeImagem(id.toString() + "." + extencaoArquivo);
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
		
		File imagem = new File(CAMINHO_IMG + produto.getNomeImagem());
		
		byte [] imagemProduto = Files.readAllBytes(imagem.toPath());
		
		String imgBase64 = Base64.encodeBase64String(imagemProduto);
		return "data:application/img;base64,"  + imgBase64;
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
