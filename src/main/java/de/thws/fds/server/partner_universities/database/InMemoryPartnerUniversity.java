package de.thws.fds.server.partner_universities.database;

import de.thws.fds.server.partner_universities.repository.PartnerUniversityRepo;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.List;

/**
 * In-Memory Database with 3 partner universities.
 * Dummy Data used for testing functionality of CRUD.
 */
@Configuration
public class InMemoryPartnerUniversity {
    @Bean
    public PartnerUniversity christUniversity() {
        return new PartnerUniversity(
                "Christ University",
                "India",
                ", Department of Information Technology",
                "www.christ-university.com",
                "Dr. Ravi Kumar",
                10,
                10,
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 8, 20)
        );
    }

    @Bean
    public PartnerUniversity lucianUniversity() {
        return new PartnerUniversity(
                "Lucian Blaga University",
                "Romania",
                "Department of Computer Science",
                "www.uni-lucian-blaga.ro",
                "Dr. Ioan Alexandru Baciu",
                25,
                10,
                LocalDate.of(2022, 2, 5),
                LocalDate.of(2022, 9, 28)
        );
    }

    @Bean
    public PartnerUniversity sheffieldUniversity() {
        return new PartnerUniversity(
                "University Of Sheffield",
                "UK",
                "CS",
                "www.uni-sheffield.uk",
                "Dr. Rose Smith",
                20,
                15,
                LocalDate.of(2023, 1, 30),
                LocalDate.of(2023, 10, 1)
        );
    }


    @Bean
    @Order(1)
    @Transactional
    public CommandLineRunner commandLineRunner(PartnerUniversityRepo partnerUniversityRepository) {
        return args -> partnerUniversityRepository.saveAll(List.of(christUniversity(), lucianUniversity(), sheffieldUniversity()));
    }
}