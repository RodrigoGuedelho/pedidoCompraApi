package br.com.guedelho.pedidoCompraApi.utils;

import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.guedelho.pedidoCompraApi.ApplicationContextLoad;
import br.com.guedelho.pedidoCompraApi.models.Usuario;
import br.com.guedelho.pedidoCompraApi.repository.UsuarioRepository;

public class Utils {
	public static boolean isEmpity(String texto) {
		return texto == null || texto == "";
	}
	
	public static String getUserNameUsuarioLogado(String token) {	
		try {
			token = token.replace("Bearer ", "");
			String partesToken[] = new String[3];	
			partesToken = token.split("\\.");	
			
			String payloadToken = new String(Base64.getDecoder().decode(partesToken[1]));
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(payloadToken);
			return node.get("sub").asText();
		} catch (Exception e) {
			return "";
		}
	}
	
	public static Usuario getUsuarioLogado(String token) {
		String userName = getUserNameUsuarioLogado(token);
		Usuario usuario = ApplicationContextLoad.getApplicationContext()
    	.getBean(UsuarioRepository.class).findUserByLogin(userName);
		
		return usuario;
	}
}
