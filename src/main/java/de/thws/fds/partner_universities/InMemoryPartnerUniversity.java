package de.thws.fds.partner_universities;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.List;

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
    public PartnerUniversity otherUniversity() {
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
    @Order(1)
    @Transactional
    public CommandLineRunner commandLineRunner(PartnerUniversityRepo partnerUniversityRepository) {
        return args -> partnerUniversityRepository.saveAll(List.of(christUniversity(), otherUniversity()));
    }
}