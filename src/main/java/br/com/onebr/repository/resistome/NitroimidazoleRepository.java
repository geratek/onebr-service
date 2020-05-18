package br.com.onebr.repository.resistome;

import br.com.onebr.model.resistome.Nitroimidazole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NitroimidazoleRepository extends JpaRepository<Nitroimidazole, Long>, ResistomeBaseRepository {

    @Query(value = "SELECT x.* FROM nitroimidazole x WHERE LOWER(x.name) = LOWER(:name)", nativeQuery = true)
    Nitroimidazole findByName(@Param("name") String name);

    @Query(value = "SELECT x.* FROM nitroimidazole x WHERE LOWER(x.name) LIKE LOWER(CONCAT('%',:name,'%'))", nativeQuery = true)
    List<Nitroimidazole> findByNameLike(@Param("name") String name);
}
