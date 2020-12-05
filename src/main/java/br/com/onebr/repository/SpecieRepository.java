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

    @Query(value = "SELECT s.* FROM specie s WHERE s.name = :name AND s.fk_specie_group = :specieGroupId", nativeQuery = true)
    Specie findByNameAndSpecieGroupId(@Param("name") String name, @Param("specieGroupId") Long specieGroupId);

    Set<Specie> findAllByIdIn(List<Long> ids);

    @Query(value = "SELECT sg.* FROM specie s INNER JOIN specie sg ON sg.id = s.fk_specie_group WHERE s.id = :specieId", nativeQuery = true)
    Specie getSpecieGroup(@Param("specieId") Long specieId);

    @Query(value = "SELECT s.* FROM specie s WHERE s.fk_specie_group IS NULL ORDER BY s.id", nativeQuery = true)
    List<Specie> findAllByOrderById();

    @Query(value = "SELECT s.* FROM specie s INNER JOIN user_specie us ON us.fk_specie = s.id "
        + "WHERE us.fk_user = :userId AND s.fk_specie_group IS NULL "
        + "ORDER BY s.id", nativeQuery = true)
    List<Specie> findAllByUserIdOrderById(@Param("userId") Long userId);

    @Query(value = "SELECT s.* FROM specie s WHERE s.fk_specie_group = :specieGroupId ORDER BY s.id", nativeQuery = true)
    List<Specie> findAllByGroupIdOrderById(@Param("specieGroupId") Long specieGroupId);
}
