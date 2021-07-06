package br.com.guedelho.pedidoCompraApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.guedelho.pedidoCompraApi.models.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	
	@Query(value = "SELECT p FROM Produto p "
			+ "where lower(p.descricao) like %:descricao% "
			+ "and (p.id = :id or :id = 0)"
			+ "and p.status = 'ATIVO'")
    public List<Produto> find(@Param("descricao") String descricao, @Param("id") Long id);
}
