package com.Pasteleria.Pasteleria.controller;

import com.Pasteleria.Pasteleria.model.Orden;
import com.Pasteleria.Pasteleria.service.OrdenService;
import com.Pasteleria.Pasteleria.service.OrdenService.LineaOrdenRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@CrossOrigin(origins = "*")
@Tag(name = "Órdenes", description = "Operaciones para gestionar las órdenes de compra")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    // ============================
    // LISTAR TODAS (ADMIN, VENDEDOR)
    // ============================
    @GetMapping
    @Operation(summary = "Lista todas las órdenes")
    public List<Orden> listarOrdenes() {
        return ordenService.listarOrdenes();
    }

    // ============================
    // OBTENER DETALLE POR ID
    // ============================
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una orden por su id")
    public ResponseEntity<Orden> obtenerOrden(@PathVariable Long id) {
        Orden orden = ordenService.obtenerPorId(id);
        if (orden == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orden);
    }

    // ============================
    // CREAR ORDEN (CLIENTE)
    // ============================
    @PostMapping
    @Operation(summary = "Crea una nueva orden")
    public ResponseEntity<Orden> crearOrden(
            @RequestParam String emailCliente,
            @RequestBody List<LineaOrdenRequest> lineas) {

        Orden orden = ordenService.crearOrden(emailCliente, lineas);
        return ResponseEntity.ok(orden);
    }
}
