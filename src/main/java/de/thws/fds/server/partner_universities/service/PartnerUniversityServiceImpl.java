package de.thws.fds.server.partner_universities.service;

import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import de.thws.fds.server.partner_universities.repository.PartnerUniversityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * This class represents the Implemented PartnerUniversity Service. For detailed insight compare with the corresponding Interface.
 */
@Service
public class PartnerUniversityServiceImpl implements PartnerUniversityService {

    @Autowired
    private PartnerUniversityRepo repository;

    @Override
    public Page<PartnerUniversity> getAllUniversities(int pageNo, int pageSize,String sortDirection) {
        Sort sort = sortDirection != null && sortDirection.equalsIgnoreCase("desc")
                ? Sort.by("name").descending()
                : Sort.by("name").ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return repository.findAll(pageable);
    }

    @Override
    public Optional<PartnerUniversity> getUniversityById(Long id) {
        return repository.findById(id);
    }

    //Filtering
    @Override
    public Page<PartnerUniversity> filterUniversities(Optional<String> country, Optional<String> name, Optional<LocalDate> spring, Optional<LocalDate> autumn, Optional<String> contactPerson, int pageNo,
                                                      int pageSize) {
        Specification<PartnerUniversity> spec = Specification.where(null);

        if (country.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("country"), country.get()));
        }
        if (name.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name.get()));
        }
        if (spring.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nextSpringSemesterStart"), spring.get()));
        }
        if (autumn.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("nextAutumnSemesterStart"), autumn.get()));
        }
        if (contactPerson.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("contactPerson"), contactPerson.get()));
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("name").ascending());
        return repository.findAll(spec, pageable);
    }

    @Override
    public PartnerUniversity createUniversity(PartnerUniversity university) {
        return repository.save(university);
    }

    @Override
    public PartnerUniversity updateUniversity(PartnerUniversity updatedUni) {
        //find updatedUni by non-changeable ID
        Optional<PartnerUniversity> optionalUni = repository.findById(updatedUni.getId());
        if (optionalUni.isPresent()) {
            PartnerUniversity oldUni = optionalUni.get();
            //update updatedUni
            oldUni.setName(updatedUni.getName());
            oldUni.setCountry(updatedUni.getCountry());
            oldUni.setDepartmentName(updatedUni.getDepartmentName());
            oldUni.setContactPerson(updatedUni.getContactPerson());
            oldUni.setOutboundStudents(updatedUni.getOutboundStudents());
            oldUni.setInboundStudents(updatedUni.getInboundStudents());
            oldUni.setNextSpringSemesterStart(updatedUni.getNextSpringSemesterStart());
            oldUni.setNextAutumnSemesterStart(updatedUni.getNextAutumnSemesterStart());
            return repository.save(oldUni);
        } else {
            //if no Uni found tu update, return empty Uni
            return new PartnerUniversity();

        }

    }

    @Override
    public void deleteUniversity(Long id) {
        repository.deleteById(id);
    }
}
