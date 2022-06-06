package ma.cam.kernal.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.cam.kernal.data.model.auth.Menu;

@Repository
public interface MenuDao extends CrudRepository<Menu, Integer> {

	@Query(value = "select a.* from menu a,ws_role r,ws_utilisateur_role ur,menu_roles mr where\n"
			+ "			a.id=mr.id_menu and MR.ID_ROLE = UR.ID_ROLE and r.id= ur.id_role and ur.id_utilisateur = :userid and a.id_menu_parent is null", nativeQuery = true)
	List<Menu> getMenuByUserQuery(@Param("userid") long userid);

}
