package com.Pasteleria.Pasteleria.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.Pasteleria.Pasteleria.model.Producto;
import com.Pasteleria.Pasteleria.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")  
@Tag(name = "Productos", description = "Operaciones relacionadas con los productos")
@CrossOrigin(origins = {"*"})
public class ProductoController {


    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos")
    public List<Producto> listarProductos() {
        return productoService.getProductos();
    }

    @PostMapping
    @Operation(summary = "Ingresar producto")
    public Producto agregarProducto(@RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Buscar producto por código")
    public Producto buscarProducto(@PathVariable int codigo) {
        return productoService.getProductoId(codigo);
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Actualizar producto por código")
    public Producto actualizarProducto(@PathVariable int codigo, @RequestBody Producto producto) {
        producto.setCodigo(codigo);
        return productoService.updateProducto(producto);
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar producto por código")
    public String eliminarProducto(@PathVariable int codigo) {
        return productoService.deleteProducto(codigo);
    }
}
