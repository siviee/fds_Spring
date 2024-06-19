package de.thws.fds.server.partner_universities.service;

import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service interface for managing {@link PartnerUniversity} entities.
 * Provides methods for CRUD operations and a filtering method with few querries.
 */


public interface PartnerUniversityService {

    /**
     * Retrieves all partner universities with pagination.
     *
     * @param pageNo        the page number
     * @param pageSize      the page size
     * @param sortDirection tells in which order to sort after name: asc or desc
     * @return a page of partner universities
     */
    Page<PartnerUniversity> getAllUniversities(int pageNo, int pageSize, String sortDirection);


    /**
     * Retrieves a specific partner university by its ID.
     *
     * @param id the university ID
     * @return an optional containing the partner university if found, otherwise empty
     */
    Optional<PartnerUniversity> getUniversityById(Long id);


    /**
     * Filters partner universities based on optional criteria.
     *
     * @param country       optional country name
     * @param name          optional university name
     * @param spring        optional start date for spring semester
     * @param autumn        optional start date for autumn semester
     * @param contactPerson optional contact person name
     * @param pageNo        the page number
     * @param pageSize      the page size
     * @return a page of filtered partner universities
     */
    Page<PartnerUniversity> filterUniversities(Optional<String> country, Optional<String> name, Optional<LocalDate> spring, Optional<LocalDate> autumn, Optional<String> contactPerson, int pageNo,
                                               int pageSize);

    /**
     * Creates a new partner university.
     *
     * @param university the university to create
     * @return the created partner university
     */
    PartnerUniversity createUniversity(PartnerUniversity university);


    /**
     * Updates an existing partner university.
     *
     * @param updatedUni the updated university details
     * @return the updated partner university
     */
    PartnerUniversity updateUniversity(PartnerUniversity updatedUni);


    /**
     * Deletes a partner university by its ID.
     *
     * @param id the university ID
     */
    void deleteUniversity(Long id);
}
