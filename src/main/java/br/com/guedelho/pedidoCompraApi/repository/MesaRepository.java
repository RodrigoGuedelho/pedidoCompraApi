package br.com.guedelho.pedidoCompraApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.guedelho.pedidoCompraApi.dto.MesaDto;
import br.com.guedelho.pedidoCompraApi.models.Mesa;
import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;

@Repository
public interface MesaRepository extends  JpaRepository<Mesa, Long>{
	
	@Query(value = "SELECT m.* FROM Mesa m where m.numero = :numero  and m.status = 'ATIVO' LIMIT 1", nativeQuery=true)
	public Mesa findByNumero(@Param("numero") int numero);
	
	@Query(value = "SELECT m FROM Mesa m where (m.numero = :numero or :numero = 0) "
			+ "and (m.id = :id or :id = 0) "
			+ "and m.status = :status ")
	public List<Mesa> find(@Param("numero") int numero, @Param("id") Long id, StatusGenerico  status);
	
	@Query(value="select m.id, m.numero from mesa m "
			+ "left join pedido p on (p.mesa_id = m.id and p.status = 'ABERTO') "
			+ "where p.id is null and m.status = 'ATIVO'  "
			+ "and (m.numero = :numero or :numero = 0);", nativeQuery=true)
	public List<MesaDto> findMesaAbertas(@Param("numero") int numero);
	
}
