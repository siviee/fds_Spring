package de.thws.fds.partner_universities;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LocalDataBase {
    private final PartnerUniversityRepo universityRepository;

    @Autowired
    public LocalDataBase(PartnerUniversityRepo universityRepository) {
        this.universityRepository = universityRepository;
    }

    @PostConstruct
    public void load() {
        universityRepository.save(new PartnerUniversity("Christ University", "India", "Department of Information Technology", "http://www.christ-uni.com", "Dr. Ravi Kumar", 10, 20, LocalDate.of(2025, 1, 25), LocalDate.of(2024, 9, 1)));
        universityRepository.save(new PartnerUniversity("Harvard University",
                "USA", "Computer Science", "http://cs.harvard.edu", "Dr. John Smith", 10, 15, LocalDate.of(2024, 1, 15), LocalDate.of(2024, 8, 20)));
        universityRepository.save(new PartnerUniversity("Oxford University", "UK", "Engineering", "http://eng.oxford.edu", "Dr. Jane Doe", 8, 12, LocalDate.of(2024, 2, 10), LocalDate.of(2024, 9, 25)));
    }
}
