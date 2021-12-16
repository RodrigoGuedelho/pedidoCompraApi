package br.com.guedelho.pedidoCompraApi.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.guedelho.pedidoCompraApi.ApplicationContextLoad;
import br.com.guedelho.pedidoCompraApi.models.Usuario;
import br.com.guedelho.pedidoCompraApi.repository.UsuarioRepository;

public class Utils {
	public static String CAMINHO_RAIZ_IMAGEM = "/opt/pedido-legal/uploads/";
	
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
	
	public static String getExtencaoArquivo(String nomeArquivo) {
		String[] nomeFileQuebrado = nomeArquivo.split("\\.");
		return nomeFileQuebrado[nomeFileQuebrado.length -1];
	}
	
	public static String uploadImagem(Long id, MultipartFile file, String caminhoImagem) throws IOException {
		String extencaoArquivo =  getExtencaoArquivo(file.getOriginalFilename());
		Path path = Paths.get(caminhoImagem);
		Files.createDirectories(path);
		Path pathImg = Paths.get(caminhoImagem + id.toString() + "." + extencaoArquivo);
		Files.write(pathImg, file.getBytes());
		return id.toString() + "." + extencaoArquivo;
	}
	
	public static String getImagem(String caminhoNome) throws IOException {
		File imagem = new File(caminhoNome);
		
		byte [] imagemProduto = Files.readAllBytes(imagem.toPath());
		
		String imgBase64 = org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(imagemProduto);
		return "data:application/img;base64,"  + imgBase64;
	}
}
