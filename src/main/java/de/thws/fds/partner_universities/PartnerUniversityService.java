package de.thws.fds.partner_universities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PartnerUniversityService {

    @Autowired
    private PartnerUniversityRepo repository;


    public List<PartnerUniversity> getAllUniversities() {
        return repository.findAll();
    }

    public Optional<PartnerUniversity> getUniversityById(Long id) {
        return repository.findById(id);
    }

    //Filtering
    public List<PartnerUniversity> filterUniversities(Optional<String> country, Optional<String> name, Optional<LocalDate> spring, Optional<LocalDate> autumn, Optional<String> contactPerson) {
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

        return repository.findAll(spec);
    }


    public PartnerUniversity createUniversity(PartnerUniversity university) {
        return repository.save(university);
    }

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

    public void deleteUniversity(Long id) {
        repository.deleteById(id);
    }
}
