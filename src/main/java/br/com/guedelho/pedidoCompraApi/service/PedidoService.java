package br.com.guedelho.pedidoCompraApi.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.guedelho.pedidoCompraApi.models.Mesa;
import br.com.guedelho.pedidoCompraApi.models.Pedido;
import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.models.StatusPedido;
import br.com.guedelho.pedidoCompraApi.repository.MesaRepository;
import br.com.guedelho.pedidoCompraApi.repository.PedidoRepository;
import br.com.guedelho.pedidoCompraApi.utils.Utils;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private MesaRepository mesaRepository;
	
	public Pedido salvar(Pedido pedido, String token) throws Exception {	
		pedido.setDataPedido(OffsetDateTime.now());
		pedido.setStatus(StatusPedido.ABERTO);
		pedido.setUsuario(Utils.getUsuarioLogado(token));
		
		Exception validacao = validar(pedido);
		if (validacao != null) {
			throw validacao;
		}
		
		pedido = pedidoRepository.save(pedido);
		return pedidoRepository.findById(pedido.getId()).get();
	}
	
	
	public Pedido mudaStatusPedido(Long id, StatusPedido status) throws Exception {
		Pedido pedido = pedidoRepository.getById(id);
		
		if (pedido == null)
			throw new Exception("Id inválido");
		if (!pedido.getStatus().equals(StatusPedido.ABERTO)) 
			throw new Exception("Pedido já está cancelado ou finalizado.");
		
		pedido.setStatus(status);
		return pedidoRepository.save(pedido);
			
	}
	
	public Pedido editar(Long id, Pedido pedido, String token) throws Exception {
		Exception validarEditar = validarEditar(id, pedido);
		if (validarEditar != null) 
			throw validarEditar;
		
		Pedido pedidoAuxiliar = pedidoRepository.findById(id).get();
		pedido.setDataPedido(pedidoAuxiliar.getDataPedido());
		pedido.setStatus(StatusPedido.ABERTO);
		pedido.setUsuario(Utils.getUsuarioLogado(token));
		pedido.setId(id);
	
		pedido = pedidoRepository.save(pedido);
		return pedidoRepository.findById(pedido.getId()).get();
	}
	
	public List<Pedido> find(LocalDate dataInicio, LocalDate dataFim, String observacao,
			Long id) {	
		return pedidoRepository.find(dataInicio.toString(), dataFim.toString(), "%" + observacao + "%", id);
	}
	
	
	private Exception validar(Pedido pedido) {
		if (pedido.getItensPedido() == null || (pedido.getItensPedido() != null && pedido.getItensPedido().isEmpty())) 
			return new Exception("Itens do pedido vazio");
		Mesa mesa = mesaRepository.findById(pedido.getMesa().getId()).get();
		
		if (mesa == null) 
			return new Exception("Mesa não preenchida ou inválida.");
		
		List<Pedido> pedidoAberto = pedidoRepository.findByMesaStatusAberto(mesa.getId()); 
		if (pedidoAberto.isEmpty()) 
			return new Exception("Existe uma mesa aberta para esse Pedido.");
		
		return null;
	}
	
	public Exception validarEditar(Long id, Pedido pedido) {
		Pedido pedidoAuxiliar = pedidoRepository.findById(id).get();	
		if (pedidoAuxiliar == null) 
			return new Exception("Id inválido.");
		if (!pedidoAuxiliar.getStatus().equals(StatusPedido.ABERTO))
			return new Exception("Não é possível editar um pedido Finalizado ou cancelado.");
		if (!pedidoAuxiliar.getStatus().equals(StatusPedido.ABERTO)) 
			return new Exception("Não é possível editar o status do pedido por essa rota."); 
		if (pedido.getItensPedido() == null || (pedido.getItensPedido() != null && pedido.getItensPedido().isEmpty())) 
			return new Exception("Itens do pedido vazio");
		Mesa mesa = mesaRepository.findById(pedido.getMesa().getId()).get();
		
		if (mesa == null) 
			return new Exception("Mesa não preenchida ou inválida.");
		
		List<Pedido> pedidoAuxliarStatus = pedidoRepository.findByMesaStatusAberto(id);
		if (!pedidoAuxliarStatus.isEmpty())
			return new Exception("Existe uma mesa aberta para esse usuário.");
		
		return null;
	}
}
