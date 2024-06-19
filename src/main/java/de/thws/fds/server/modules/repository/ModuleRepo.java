package de.thws.fds.server.modules.repository;

import de.thws.fds.server.modules.model.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for managing {@link Module} entities.
 * Provides methods for common CRUD operations as well as queries: findByPartnerUniversityId
 * and findByPartnerUniversityIdAndId(finds Module by its id  and the id of the uni)
 * Extends {@link JpaRepository} for basic CRUD operations and {@link JpaSpecificationExecutor}
 * for query specification execution.
 */
public interface ModuleRepo extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {
    Page<Module> findByPartnerUniversityId(Long partnerUniversityId, Pageable pageable);

    Module findByPartnerUniversityIdAndId(Long universityId, Long moduleId);
}
