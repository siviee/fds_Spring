package de.thws.fds.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModuleRepo extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {
    Module findByPartnerUniversityId(Long partnerUniversityId);

    Module findByPartnerUniversityIdAndId(Long universityId, Long moduleId);
}
