package br.com.onebr.repository;

import br.com.onebr.model.SCCMecElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SCCMecElementRepository extends JpaRepository<SCCMecElement, Long> {

    SCCMecElement findByName(String name);
}
