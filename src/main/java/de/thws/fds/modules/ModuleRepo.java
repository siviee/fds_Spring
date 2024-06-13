package de.thws.fds.modules;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModuleRepo extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {
    Page<Module> findByPartnerUniversityId(Long partnerUniversityId, Pageable pageable);

    Module findByPartnerUniversityIdAndId(Long universityId, Long moduleId);
}
