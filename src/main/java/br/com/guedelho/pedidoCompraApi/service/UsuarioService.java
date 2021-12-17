package br.com.guedelho.pedidoCompraApi.service;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.guedelho.pedidoCompraApi.models.Mesa;
import br.com.guedelho.pedidoCompraApi.models.Produto;
import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.models.Usuario;
import br.com.guedelho.pedidoCompraApi.repository.UsuarioRepository;
import br.com.guedelho.pedidoCompraApi.utils.Utils;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	private String CAMINHO_IMG = Utils.CAMINHO_RAIZ_IMAGEM + "usuarios/";
	
	public Usuario salvar(Usuario usuario) throws Exception {
		Exception validaUsuario = validaUsuario(usuario);
		
		if (validaUsuario != null)
			throw validaUsuario;
		usuario.setStatus(StatusGenerico.ATIVO);
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}
	
	public Usuario editar(Usuario usuario, Long id) throws Exception {
		Exception validaUsuario = validaUsuarioEditar(usuario, id);
		
		if (validaUsuario != null)
			throw validaUsuario;
		
		usuario.setId(id);
		usuario.setStatus(StatusGenerico.ATIVO);
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}
	
	public Exception validaUsuario(Usuario usuario) {
		Usuario usuarioExiste = usuarioRepository.findUserByLogin(usuario.getLogin());
		if(usuarioExiste != null) {
			return new Exception("Usuário com esse nome já existe");
		}
		
		return null;
	}
	
	public Exception validaUsuarioEditar(Usuario usuario, Long id) {
		Usuario usuarioExiste = usuarioRepository.findUserByLogin(usuario.getLogin());
		Usuario usuarioAuxiliar = usuarioRepository.findById(id).get();
		if (usuarioAuxiliar == null)
			return new Exception("Usuário com esse id não existe");
		if(usuarioExiste != null && !usuarioExiste.getId().equals(id)) {
			return new Exception("Usuário com esse nome já existe");
		}
		if(!usuarioAuxiliar.getStatus().equals(StatusGenerico.ATIVO)) {
			return new Exception("Usuário Está cancelado");
		}
		
		return null;
	}
	
	public Usuario cancelar(Long id) throws Exception {
		Usuario usuarioAuxiliar = usuarioRepository.findById(id).get();
		if (usuarioAuxiliar.getStatus().equals(StatusGenerico.CANCELADO) && usuarioAuxiliar != null) 
			throw new Exception("Usuário está cancelada.");
		
		usuarioAuxiliar.setStatus(StatusGenerico.CANCELADO);
		return usuarioRepository.save(usuarioAuxiliar);
	}
	
	public List<Usuario> find(String login, String nome, Long id, StatusGenerico status) {
		return usuarioRepository.find("%" + login + "%", "%" + nome + "%", id, status);
	}
	
	public Usuario uploadImagem(Long id, MultipartFile file) throws Exception {
		if (file.isEmpty())
			throw new Exception("Upload vazio.");
		Usuario usuarioAxiliar = usuarioRepository.findById(id).get();
		if (usuarioAxiliar == null)
			throw new Exception("Id do produto invalido.");	
		try {
			String nomeImagem = Utils.uploadImagem(id, file, CAMINHO_IMG);	
			usuarioAxiliar.setNomeImagem(nomeImagem);
			return usuarioRepository.save(usuarioAxiliar);	
		} catch (Exception e) {
			throw new Exception("Erro inesperado ao fazer o upload do arquivo");
		}
	}
	
	public String findImgById(Long usuarioId, ServletContext servletContext) throws Exception {
		Usuario usuario = usuarioRepository.findById(usuarioId).get();	
		if (usuario == null)
			throw new Exception("Id do usuário invalido.");
		if (usuario.getNomeImagem().isEmpty())
			throw new Exception("Usuário Não possui imagem");
		
		return Utils.getImagem(CAMINHO_IMG + usuario.getNomeImagem());
	}
	
	public String findImgByLogin(String login, ServletContext servletContext) throws Exception {
		Usuario usuario = usuarioRepository.findUserByLogin(login);
		if (usuario == null)
			throw new Exception("Id do usuário invalido.");
		if (usuario.getNomeImagem().isEmpty())
			throw new Exception("Usuário Não possui imagem");
		
		return Utils.getImagem(CAMINHO_IMG + usuario.getNomeImagem());
	}
	
}
