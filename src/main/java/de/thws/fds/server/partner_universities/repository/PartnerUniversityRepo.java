package de.thws.fds.server.partner_universities.repository;

import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for managing {@link PartnerUniversity} entities.
 * Provides methods for common CRUD operations.
 * <p>
 * Extends {@link JpaRepository} for basic CRUD operations and {@link JpaSpecificationExecutor}
 * for query specification execution if provided (in this case here are no specific queries, as the filtering for partnerUniversity
 * happens with the help of a method, see PartnerUniversityServiceImpl.
 */
public interface PartnerUniversityRepo extends JpaRepository<PartnerUniversity, Long>, JpaSpecificationExecutor<PartnerUniversity> {

}
