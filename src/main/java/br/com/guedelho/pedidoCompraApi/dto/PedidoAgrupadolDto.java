package br.com.guedelho.pedidoCompraApi.dto;

import java.time.OffsetDateTime;
import java.util.Date;

public interface PedidoAgrupadolDto {
	public Long getId();
	public Date getDataPedido();
	public int getNumeroMesa();
	public double getValorTotal();
	
	
}
