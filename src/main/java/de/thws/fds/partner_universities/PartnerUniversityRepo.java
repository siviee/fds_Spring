package de.thws.fds.partner_universities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PartnerUniversityRepo  extends JpaRepository<PartnerUniversity, Long>, JpaSpecificationExecutor<PartnerUniversity> {

}
