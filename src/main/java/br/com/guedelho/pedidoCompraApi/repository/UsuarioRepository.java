package br.com.guedelho.pedidoCompraApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.guedelho.pedidoCompraApi.models.StatusGenerico;
import br.com.guedelho.pedidoCompraApi.models.Usuario;



@Repository
public interface UsuarioRepository  extends CrudRepository<Usuario, Long>{
	
	@Query("select u from Usuario u where u.login = ?1 and u.status = 'ATIVO'")
	Usuario findUserByLogin(String login);
	
	@Query("Select u from Usuario u where (u.status = :status or :status is null) "
			+ "and lower(u.login) like lower(:login) and lower(u.nome) like lower(:nome) "
			+ "and (u.id = :id or :id = 0)")
	List<Usuario> find(@Param("login") String login, @Param("nome") String nome, 
			@Param("id") Long id, @Param("status") StatusGenerico status);

}
