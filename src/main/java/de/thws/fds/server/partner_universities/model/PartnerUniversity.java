package de.thws.fds.server.partner_universities.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.thws.fds.server.modules.model.Module;

/**
 * Represents a  partner university.
 * A university is a primary-ressource and contains information like name of the university, country,
 * department name, department URL, contact person, outboundStudents(number of students university can send),
 * inboundStudents(number of students university can accept),nextSpringSemester(Date when the spring semester starts),
 * nextAutumnSemester(Date when the autumn semester starts) and a list of modules.
 */
@Entity
@Table
public class PartnerUniversity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;
    private String departmentName;
    private String departmentUrl;
    private String contactPerson;
    private int outboundStudents;
    private int inboundStudents;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate nextSpringSemesterStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate nextAutumnSemesterStart;


    @JsonManagedReference
    @OneToMany(mappedBy = "partnerUniversity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module> modules;


    public PartnerUniversity() {
    }

    public PartnerUniversity(Long id, String name, String country, String departmentName, String departmentUrl, String contactPerson, int outboundStudents, int inboundStudents, LocalDate nextSpringSemesterStart, LocalDate nextAutumnSemesterStart) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.departmentName = departmentName;
        this.departmentUrl = departmentUrl;
        this.contactPerson = contactPerson;
        this.outboundStudents = outboundStudents;
        this.inboundStudents = inboundStudents;
        this.nextSpringSemesterStart = nextSpringSemesterStart;
        this.nextAutumnSemesterStart = nextAutumnSemesterStart;

    }

    public PartnerUniversity(String name, String country, String departmentName, String departmentUrl, String contactPerson, int outboundStudents, int inboundStudents, LocalDate nextSpringSemesterStart, LocalDate nextAutumnSemesterStart) {

        this.name = name;
        this.country = country;
        this.departmentName = departmentName;
        this.departmentUrl = departmentUrl;
        this.contactPerson = contactPerson;
        this.outboundStudents = outboundStudents;
        this.inboundStudents = inboundStudents;
        this.nextSpringSemesterStart = nextSpringSemesterStart;
        this.nextAutumnSemesterStart = nextAutumnSemesterStart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentUrl() {
        return departmentUrl;
    }

    public void setDepartmentUrl(String departmentUrl) {
        this.departmentUrl = departmentUrl;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getOutboundStudents() {
        return outboundStudents;
    }

    public void setOutboundStudents(int outboundStudents) {
        this.outboundStudents = outboundStudents;
    }

    public int getInboundStudents() {
        return inboundStudents;
    }

    public void setInboundStudents(int inboundStudents) {
        this.inboundStudents = inboundStudents;
    }

    public LocalDate getNextSpringSemesterStart() {
        return nextSpringSemesterStart;
    }

    public void setNextSpringSemesterStart(LocalDate nextSpringSemesterStart) {
        this.nextSpringSemesterStart = nextSpringSemesterStart;
    }

    public LocalDate getNextAutumnSemesterStart() {
        return nextAutumnSemesterStart;
    }

    public void setNextAutumnSemesterStart(LocalDate nextAutumnSemesterStart) {
        this.nextAutumnSemesterStart = nextAutumnSemesterStart;
    }
    //NEW

    public List<Module> getModules() {
        return modules;
    }

    @JsonSetter
    public void setModules(List<Module> modules) {
        this.modules = modules != null ? modules : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "PartnerUniversity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", website='" + departmentUrl + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", outboundStudents=" + outboundStudents +
                ", inboundStudents=" + inboundStudents +
                ", nextSpringSemesterStart=" + nextSpringSemesterStart +
                ", nextAutumnSemesterStart=" + nextAutumnSemesterStart +
                '}';
    }
}


