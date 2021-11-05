package br.com.guedelho.pedidoCompraApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.guedelho.pedidoCompraApi.dto.MesaDto;
import br.com.guedelho.pedidoCompraApi.models.Mesa;
import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.repository.MesaRepository;
import br.com.guedelho.pedidoCompraApi.utils.Utils;

@Service
public class MesaService {
	
	@Autowired
	private MesaRepository mesaRepository;
	
	public Mesa salvar(Mesa mesa, String token) throws Exception {
		Exception validacao = validar(mesa);
		
		if (validacao != null)
			throw validacao;
		
		mesa.setStatus(StatusGenerico.ATIVO);
		mesa.setUsuario(Utils.getUsuarioLogado(token));
		
		return mesaRepository.save(mesa);
	}
	
	public Mesa cancelar(Long id, String token) throws Exception {
		Mesa mesaAuxiliar = mesaRepository.findById(id).get();
		if (mesaAuxiliar.getStatus().equals(StatusGenerico.CANCELADO) && mesaAuxiliar != null) 
			throw new Exception("Mesa está cancelada.");
		
		mesaAuxiliar.setStatus(StatusGenerico.CANCELADO);
		return mesaRepository.save(mesaAuxiliar);
	}
	
	public Exception validar(Mesa mesa) {
		Mesa mesaAuxiliar = mesaRepository.findByNumero(mesa.getNumero());
		
		if((mesaAuxiliar != null && mesa.getId() == null) 
				|| (mesaAuxiliar != null && mesa.getId() != null 
				&& !mesa.getId().equals(mesaAuxiliar.getId())))
			return new Exception("Número de mesa já existe");
		if (mesa.getId() != null) {
			mesaAuxiliar = mesaRepository.findById(mesa.getId()).get();
			if (mesaAuxiliar != null && mesaAuxiliar.getStatus().equals(StatusGenerico.CANCELADO)) 
				return new Exception("Mesa está cancelada.");
		}
		return null;
	}
	
	public List<Mesa> find(int numero, Long id, StatusGenerico status) {
		if (id == null)
			id = 0L;
		return mesaRepository.find(numero, id, status);
	}
	
	public List<MesaDto> findMesasAbertas(int numero) {
		return mesaRepository.findMesaAbertas(numero);
	}
}
