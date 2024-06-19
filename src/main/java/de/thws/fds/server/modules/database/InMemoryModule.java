package de.thws.fds.server.modules.database;

import de.thws.fds.server.modules.model.Module;
import de.thws.fds.server.modules.repository.ModuleRepo;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import de.thws.fds.server.partner_universities.repository.PartnerUniversityRepo;

import java.util.List;

/**
 * In-Memory Database with 4 Modules assigned individually to the existing universities.
 * Dummy Data used for testing functionality of CRUD.
 *
 */

@Configuration
public class InMemoryModule {
    @Bean
    @Order(2)
    @Transactional
    public CommandLineRunner uniModuleCommandLineRunner(PartnerUniversityRepo partnerUniversityRepository, ModuleRepo uniModuleRepository) {
        return args -> {
            PartnerUniversity christUniversity = partnerUniversityRepository.findById(1L).orElse(null);
            PartnerUniversity lucianUniversity = partnerUniversityRepository.findById(2L).orElse(null);
            PartnerUniversity sheffieldUniversity = partnerUniversityRepository.findById(3L).orElse(null);

            if (christUniversity != null && lucianUniversity != null && sheffieldUniversity!=null) {
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
                Module os= new Module(
                        "Operating Systems",
                        3,
                        10,
                        sheffieldUniversity
                );

                uniModuleRepository.saveAll(List.of(quantumComputing, fds, dmds,os));
            }
        };
    }
}