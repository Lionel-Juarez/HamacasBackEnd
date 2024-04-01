package com.example.hamacasbackend;

import com.example.hamacasbackend.entidades.Report;
import com.example.hamacasbackend.repositorios.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reportapi")
public class ReportController {
    private final ReportRepository reportRepository;

    @Autowired
    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports(){
        List<Report> reports = new ArrayList<>();
        reportRepository.findAll().forEach(reports::add);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/newReport")
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        try {
            Report createdReport = reportRepository.save(report);
            return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getReport/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable("id") Long id) {
        Optional<Report> reportFound = reportRepository.findById(id);
        return reportFound.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateReport/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable("id") Long id, @RequestBody Report reportDetails) {
        return reportRepository.findById(id).map(report -> {
            report.setTitle(reportDetails.getTitle());
            report.setDescription(reportDetails.getDescription());
            report.setState(reportDetails.getState());
            report.setFullComment(reportDetails.getFullComment());
            report.setCreationDate(reportDetails.getCreationDate());
            report.setCreatedBy(reportDetails.getCreatedBy());
            Report updatedReport = reportRepository.save(report);
            return new ResponseEntity<>(updatedReport, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteReport/{id}")
    public ResponseEntity<HttpStatus> deleteReport(@PathVariable("id") Long id) {
        try {
            reportRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
