package de.thws.fds.partner_universities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PartnerUniversity addOrUpdateUniversity(PartnerUniversity university) {
        return repository.save(university);
    }

    public void deleteUniversity(Long id) {
        repository.deleteById(id);
    }
}
