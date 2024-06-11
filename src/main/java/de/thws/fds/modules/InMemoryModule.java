package de.thws.fds.modules;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import de.thws.fds.partner_universities.PartnerUniversity;
import de.thws.fds.partner_universities.PartnerUniversityRepo;

import java.util.List;

@Configuration
public class InMemoryModule {

    @Bean
    @Order(2)
    @Transactional
    public CommandLineRunner uniModuleCommandLineRunner(PartnerUniversityRepo partnerUniversityRepository, ModuleRepo uniModuleRepository) {
        return args -> {
            PartnerUniversity christUniversity = partnerUniversityRepository.findById(1L).orElse(null);
            PartnerUniversity lucianUniversity = partnerUniversityRepository.findById(2L).orElse(null);

            if (christUniversity != null && lucianUniversity != null) {
                Module quantumComputing = new Module(
                        "Quantum Computing",
                        1,
                        2,
                        christUniversity
                );

                Module fds = new Module(
                        "Foundation of Distributed Systems",
                        4,
                        15,
                        christUniversity
                );

                Module dmds = new Module(
                        "Data Management And Data Science",
                        3,
                        10,
                        lucianUniversity
                );

                uniModuleRepository.saveAll(List.of(quantumComputing, fds, dmds));
            }
        };
    }
}