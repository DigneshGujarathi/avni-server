package org.openchs.web;

import org.openchs.dao.ReportAdminFormRepository;
import org.openchs.domain.ReportAdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

// @CrossOrigin(origins = "http://localhost:4200")
@RestController
// @RequestMapping("/api")
public class ReportAdminFormController {

    @Autowired
    ReportAdminFormRepository repository;

    @GetMapping("/web/reports")
    @PreAuthorize(value = "hasAnyAuthority('admin','organisation_admin')")
    public Page<ReportAdminForm> getQuestions(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostMapping(value = "/web/report/create")
    @PreAuthorize(value = "hasAnyAuthority('admin','organisation_admin')")
    @Transactional
    public Object saveReportTypeForWeb(@Valid @RequestBody ReportAdminForm reportAdminForm) {
        System.err.print(reportAdminForm.getReport_name());
        if (reportAdminForm.getReport_name().trim().equals(""))
             return ("Name can not be empty");
        return repository.save(reportAdminForm);
    }

    @DeleteMapping("/web/report/delete/{id}")
    @PreAuthorize(value = "hasAnyAuthority('admin','organisation_admin')")
    public void deleteReport(@PathVariable("id") long id) {
        System.out.println("Delete Customer with ID = " + id + "...");
        repository.deleteById(id);
//        return new ResponseEntity<>("Customer has been deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/report/delete")
    public ResponseEntity<String> deleteAllReport() {
        System.out.println("Delete All Customers...");

        repository.deleteAll();

        return new ResponseEntity<>("All report have been deleted!", HttpStatus.OK);
    }
}
