package br.com.guedelho.pedidoCompraApi.requestModels;

import java.time.OffsetDateTime;
import java.util.List;

public class PedidoRequest {
	private Long id;
	private OffsetDateTime dataPedido;
	private Long usuarioId;	
	private Long mesaId;
	private List<ItemPedidoRequest> itensPedido;
	private String observacao;
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
	public Long getMesaId() {
		return mesaId;
	}
	public void setMesaId(Long mesaId) {
		this.mesaId = mesaId;
	}
	public List<ItemPedidoRequest> getItensPedido() {
		return itensPedido;
	}
	public void setItensPedido(List<ItemPedidoRequest> itensPedido) {
		this.itensPedido = itensPedido;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
