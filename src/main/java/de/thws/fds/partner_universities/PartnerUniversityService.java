package de.thws.fds.partner_universities;

import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface PartnerUniversityService {
    Page<PartnerUniversity> getAllUniversities(int pageNo, int pageSize);

    Optional<PartnerUniversity> getUniversityById(Long id);

    Page<PartnerUniversity> filterUniversities(Optional<String> country, Optional<String> name, Optional<LocalDate> spring, Optional<LocalDate> autumn, Optional<String> contactPerson, int pageNo,
                                               int pageSize);

    PartnerUniversity createUniversity(PartnerUniversity university);

    PartnerUniversity updateUniversity(PartnerUniversity updatedUni);

    void deleteUniversity(Long id);
}
