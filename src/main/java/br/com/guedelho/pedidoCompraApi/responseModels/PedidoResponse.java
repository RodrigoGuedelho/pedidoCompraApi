package br.com.guedelho.pedidoCompraApi.responseModels;

import java.time.OffsetDateTime;
import java.util.List;

import br.com.guedelho.pedidoCompraApi.models.StatusPedido;

public class PedidoResponse {
	private Long id;
	private OffsetDateTime dataPedido;
	private Long usuarioId;	
	private String usuarioNome;
	private Long mesaId;
	private Long mesaNumero;
	private List<ItemPedidoResponse> itensPedido;
	private String observacao;
	private StatusPedido status;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public OffsetDateTime getDataPedido() {
		return dataPedido;
	}
	public void setDataPedido(OffsetDateTime dataPedido) {
		this.dataPedido = dataPedido;
	}
	public Long getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public String getUsuarioNome() {
		return usuarioNome;
	}
	public void setUsuarioNome(String usuarioNome) {
		this.usuarioNome = usuarioNome;
	}
	public Long getMesaId() {
		return mesaId;
	}
	public void setMesaId(Long mesaId) {
		this.mesaId = mesaId;
	}
	public Long getMesaNumero() {
		return mesaNumero;
	}
	public void setMesaNumero(Long mesaNumero) {
		this.mesaNumero = mesaNumero;
	}
	public List<ItemPedidoResponse> getItensPedido() {
		return itensPedido;
	}
	public void setItensPedido(List<ItemPedidoResponse> itensPedido) {
		this.itensPedido = itensPedido;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public StatusPedido getStatus() {
		return status;
	}
	public void setStatus(StatusPedido status) {
		this.status = status;
	}
	
	
}
