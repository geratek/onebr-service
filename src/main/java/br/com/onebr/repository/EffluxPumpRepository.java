package br.com.onebr.repository;

import br.com.onebr.model.EffluxPump;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EffluxPumpRepository extends JpaRepository<EffluxPump, Long> {

    @Query("SELECT ep FROM EffluxPump ep WHERE ep.name IN (:names)")
    List<EffluxPump> findByNameIn(@Param("names") List<String> names);

    EffluxPump findOneByName(String name);

    Set<EffluxPump> findAllByIdIn(List<Long> ids);
}
