package br.com.guedelho.pedidoCompraApi.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.guedelho.pedidoCompraApi.models.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	@Query("SELECT p from Pedido p "
			+ "where (date(p.dataPedido) between date(:dataInicio) and date(:dataFim) ) "
			+ "and (p.observacao like :observacao) and (p.id = :id or :id = 0) "
		)
	public List<Pedido> find(@Param("dataInicio") String dataInicio,  
			@Param("dataFim") String dataFim, @Param("observacao") String observacao,
			@Param("id") Long id);
	
	@Query("SELECT p from Pedido p where p.status = 'ABERTO' and p.mesa.id = :idMesa")
	public List<Pedido> findByMesaStatusAberto(@Param("idMesa") Long idMesa);
	@Query("SELECT p from Pedido p where p.status = 'ABERTO' and p.mesa.id = :idMesa "
			+ "and p.id <> :idPedido")
	public List<Pedido> findByMesaStatusAberto(@Param("idMesa") Long idMesa, @Param("idPedido") Long idPedido);
}
