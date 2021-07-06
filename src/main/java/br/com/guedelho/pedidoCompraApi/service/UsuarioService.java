package br.com.guedelho.pedidoCompraApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.models.Usuario;
import br.com.guedelho.pedidoCompraApi.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Usuario salvar(Usuario usuario) throws Exception {
		Exception validaUsuario = validaUsuario(usuario);
		
		if (validaUsuario != null)
			throw validaUsuario;
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
}
