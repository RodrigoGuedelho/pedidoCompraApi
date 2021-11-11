package br.com.guedelho.pedidoCompraApi.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.guedelho.pedidoCompraApi.dto.PedidoAgrupadolDto;
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
	@Autowired
	private ServiceRelatorio serviceRelatorio;
	
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
	
	public String getRelatorio(Date dataInicio, Date dataFim, StatusPedido status, ServletContext servletContext) throws Exception {
		Map<String, Object>  params = new HashMap<String, Object>();
		
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatPadraoBrasileiro =  new SimpleDateFormat("dd-MM-yyyy");
		
		params.put("data_inicio", format.format(dataInicio));
		params.put("data_fim", format.format(dataFim));
		params.put("filtros", formatPadraoBrasileiro.format(dataInicio) 
				+ " à " +  formatPadraoBrasileiro.format(dataFim));
		params.put("status", status.toString());
		
		byte [] relatorio = serviceRelatorio.gerarRelatorio("relatorioPedidos", servletContext, params);
		
		String pdfase64 = Base64.encodeBase64String(relatorio);
		return "data:application/pdf;base64,"  + pdfase64;
	}
	
	public String getRelatorioVisualizar(Long pedidoId, ServletContext servletContext) throws Exception {
		Map<String, Object>  params = new HashMap<String, Object>();
		
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatPadraoBrasileiro =  new SimpleDateFormat("dd-MM-yyyy");
		
		params.put("pedido_id", pedidoId);
		
		byte [] relatorio = serviceRelatorio.gerarRelatorio("relatorioPedidosVisualizar", servletContext, params);
		
		String pdfase64 = Base64.encodeBase64String(relatorio);
		return "data:application/pdf;base64,"  + pdfase64;
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
			Long id, StatusPedido status) {	
		return pedidoRepository.find(dataInicio.toString(), dataFim.toString(), "%" + observacao + "%", id, status);
	}
	
	public List<PedidoAgrupadolDto> findAgrupado(LocalDate dataInicio, LocalDate dataFim, StatusPedido status) {
		return pedidoRepository.findPedidoAgrupado(dataInicio.toString(), dataFim.toString(), status != null ? status.toString() : null);
	}
	
	public Pedido findById(Long id) {
		return pedidoRepository.findById(id).get();
	}
	
	
	private Exception validar(Pedido pedido) {
		if (pedido.getItensPedido() == null || (pedido.getItensPedido() != null && pedido.getItensPedido().isEmpty())) 
			return new Exception("Itens do pedido vazio");
		Mesa mesa = mesaRepository.findById(pedido.getMesa().getId()).get();
		
		if (mesa == null) 
			return new Exception("Mesa não preenchida ou inválida.");
		
		List<Pedido> pedidoAberto = pedidoRepository.findByMesaStatusAberto(mesa.getId()); 
		if (!pedidoAberto.isEmpty()) 
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
