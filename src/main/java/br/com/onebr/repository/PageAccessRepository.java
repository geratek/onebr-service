package br.com.onebr.repository;

import br.com.onebr.model.PageAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PageAccessRepository extends JpaRepository<PageAccess, Long> {

    /**
     * Atomic "insert or increment" for a page path. Relies on the unique index
     * {@code page_access_path_idx}; concurrent hits on the same path are
     * serialized by Postgres, so no read-modify-write race in the application.
     */
    @Modifying
    @Query(value = "INSERT INTO page_access (path, hits) VALUES (:path, 1) "
        + "ON CONFLICT (path) DO UPDATE SET hits = page_access.hits + 1, updated_at = now()",
        nativeQuery = true)
    void registerHit(@Param("path") String path);

    @Query(value = "SELECT hits FROM page_access WHERE path = :path", nativeQuery = true)
    Long findHitsByPath(@Param("path") String path);
}
