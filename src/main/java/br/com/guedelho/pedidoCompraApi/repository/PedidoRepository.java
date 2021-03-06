package br.com.guedelho.pedidoCompraApi.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.guedelho.pedidoCompraApi.dto.PedidoAgrupadolDto;
import br.com.guedelho.pedidoCompraApi.models.Pedido;
import br.com.guedelho.pedidoCompraApi.models.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	@Query("SELECT p from Pedido p "
			+ "where (date(p.dataPedido) between date(:dataInicio) and date(:dataFim) ) "
			+ "and (p.observacao like :observacao) and (p.id = :id or :id = 0) "
			+ "and (p.status = :status or :status = null)"
		)
	public List<Pedido> find(@Param("dataInicio") String dataInicio,  
			@Param("dataFim") String dataFim, @Param("observacao") String observacao,
			@Param("id") Long id, @Param("status") StatusPedido status);
	
	@Query(value="SELECT p.id, p.data_pedido as dataPedido, m.numero as numeroMesa, sum(ip.preco * ip.quantidade) as valorTotal "
			+ "from pedido p "
			+ "join pedido_itens_pedido pip on (pip.pedido_id = p.id) "
			+ "join item_pedido ip on (ip.id = pip.itens_pedido_id)"
			+ "join mesa m on (m.id = p.mesa_id) "
			+ "where (date(p.data_pedido) between date(:dataInicio) and date(:dataFim) ) "
			+ "and (p.status = :status or :status is null) "
			+ "group by p.id, m.numero, p.data_pedido ", nativeQuery = true
		)
	public List<PedidoAgrupadolDto> findPedidoAgrupado(@Param("dataInicio") String dataInicio,  
			@Param("dataFim") String dataFim, @Param("status") String status);
	
	@Query("SELECT p from Pedido p where p.status = 'ABERTO' and p.mesa.id = :idMesa")
	public List<Pedido> findByMesaStatusAberto(@Param("idMesa") Long idMesa);
	@Query("SELECT p from Pedido p where p.status = 'ABERTO' and p.mesa.id = :idMesa "
			+ "and p.id <> :idPedido")
	public List<Pedido> findByMesaStatusAberto(@Param("idMesa") Long idMesa, @Param("idPedido") Long idPedido);
}
