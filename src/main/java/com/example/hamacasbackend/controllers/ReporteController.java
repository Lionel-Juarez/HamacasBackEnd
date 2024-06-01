package com.example.hamacasbackend.controllers;
import com.example.hamacasbackend.entidades.reportes.Reporte;
import com.example.hamacasbackend.repositorios.ReporteRepositorio;
import com.example.hamacasbackend.repositorios.UsuarioRepositorio; // Asumiendo que tienes un repositorio para Usuario
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final ReporteRepositorio reportRepository;

    @Autowired
    public ReporteController(ReporteRepositorio reportRepository, UsuarioRepositorio usuarioRepositorio) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/")
    public ResponseEntity<Page<Reporte>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reporte> reportPage = reportRepository.findAll(pageable);
        return new ResponseEntity<>(reportPage, HttpStatus.OK);
    }


    @PostMapping("/newReport")
    public ResponseEntity<Reporte> createReport(@RequestBody Reporte reporte) {
        System.out.println("Creating reporte with data: " + reporte);
        try {
            Reporte createdReport = reportRepository.save(reporte);
            return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/getReport/{id}")
    public ResponseEntity<Reporte> getReportById(@PathVariable("id") Long id) {
        Optional<Reporte> reportFound = reportRepository.findById(id);
        return reportFound.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
