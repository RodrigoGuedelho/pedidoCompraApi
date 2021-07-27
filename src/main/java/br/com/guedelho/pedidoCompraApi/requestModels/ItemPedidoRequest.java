package br.com.guedelho.pedidoCompraApi.requestModels;

public class ItemPedidoRequest {
	private Long id;
	private Long produtoId;
	private double quantidade;
	private double preco;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProdutoId() {
		return produtoId;
	}
	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}
	public double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	@Override
	public String toString() {
		return "ItemPedidoRequest [id=" + id + ", produtoId=" + produtoId + ", quantidade=" + quantidade + ", preco="
				+ preco + "]";
	}
	
	
}
