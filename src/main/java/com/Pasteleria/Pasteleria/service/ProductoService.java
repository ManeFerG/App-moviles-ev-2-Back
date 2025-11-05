package com.Pasteleria.Pasteleria.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.Pasteleria.Pasteleria.model.Producto;
import com.Pasteleria.Pasteleria.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> getProductos() {
        
        return productoRepository.findAll();
    }

    public Producto saveProducto(Producto producto) {
        
        return productoRepository.save(producto);
    }

    public Producto getProductoId(int id) {
        
        return productoRepository.findById(id).orElse(null);
    }

    public Producto updateProducto(Producto producto) {
      
        return productoRepository.save(producto);
    }

    public String deleteProducto(int id) {
        
        productoRepository.deleteById(id);
        return "Producto eliminado";
    }
}