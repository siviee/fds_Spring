package de.thws.fds.server.partner_universities.repository;

import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PartnerUniversityRepo  extends JpaRepository<PartnerUniversity, Long>, JpaSpecificationExecutor<PartnerUniversity> {

}
