package br.com.onebr.repository;

import br.com.onebr.model.Specie;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecieRepository extends JpaRepository<Specie, Long> {

    Specie findByName(String name);

    Set<Specie> findAllByIdIn(List<Long> ids);

    @Query(value = "SELECT s.* FROM specie s WHERE s.fk_specie_group IS NULL ORDER BY s.id", nativeQuery = true)
    List<Specie> findAllByOrderById();

    @Query(value = "SELECT s.* FROM specie s INNER JOIN user_specie us ON us.fk_specie = s.id "
        + "WHERE us.fk_user = :userId AND s.fk_specie_group IS NULL "
        + "ORDER BY s.id", nativeQuery = true)
    List<Specie> findAllByUserIdOrderById(@Param("userId") Long userId);

    @Query(value = "SELECT s.* FROM specie s WHERE s.fk_specie_group = :specieGroupId ORDER BY s.id", nativeQuery = true)
    List<Specie> findAllByGroupIdOrderById(@Param("specieGroupId") Long specieGroupId);
}
