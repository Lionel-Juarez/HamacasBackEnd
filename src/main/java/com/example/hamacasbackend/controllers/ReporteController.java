package com.example.hamacasbackend.controllers;

import com.example.hamacasbackend.entidades.reportes.Reporte;
import com.example.hamacasbackend.entidades.usuarios.Usuario;
import com.example.hamacasbackend.repositorios.ReporteRepositorio;
import com.example.hamacasbackend.repositorios.UsuarioRepositorio; // Asumiendo que tienes un repositorio para Usuario
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final ReporteRepositorio reportRepository;
    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public ReporteController(ReporteRepositorio reportRepository, UsuarioRepositorio usuarioRepositorio) {
        this.reportRepository = reportRepository;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping("/")
    public ResponseEntity<List<Reporte>> getAllReports(){
        List<Reporte> reports = new ArrayList<>();
        reportRepository.findAll().forEach(reports::add);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/newReport")
    public ResponseEntity<Reporte> createReport(@RequestBody Reporte report) {
        try {
            Reporte createdReport = reportRepository.save(report);
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

    @PutMapping("/updateReport/{id}")
    public ResponseEntity<Reporte> updateReport(@PathVariable("id") Long id, @RequestBody Reporte reportDetails) {
        return reportRepository.findById(id).map(report -> {
            report.setTitulo(reportDetails.getTitulo());
            report.setEstado(reportDetails.getEstado());
            report.setComentarioCompleto(reportDetails.getComentarioCompleto());
            report.setFechaCreacion(reportDetails.getFechaCreacion());
            if (reportDetails.getCreadoPor() != null && reportDetails.getCreadoPor().getId() != null) {
                Usuario usuario = usuarioRepositorio.findById(reportDetails.getCreadoPor().getId()).orElse(null);
                report.setCreadoPor(usuario);
            }
            Reporte updatedReport = reportRepository.save(report);
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
