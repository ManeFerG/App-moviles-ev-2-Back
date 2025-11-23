package com.Pasteleria.Pasteleria.service;

import com.Pasteleria.Pasteleria.model.DetalleOrden;
import com.Pasteleria.Pasteleria.model.Orden;
import com.Pasteleria.Pasteleria.model.Producto;
import com.Pasteleria.Pasteleria.model.Usuario;
import com.Pasteleria.Pasteleria.repository.OrdenRepository;
import com.Pasteleria.Pasteleria.repository.ProductoRepository;
import com.Pasteleria.Pasteleria.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // LISTAR TODAS
    public List<Orden> listarOrdenes() {
        return ordenRepository.findAll();
    }

    // OBTENER POR ID
    public Orden obtenerPorId(Long id) {
        return ordenRepository.findById(id).orElse(null);
    }

    // CREAR ORDEN SIMPLE (cliente por email, lista de productos)
    public Orden crearOrden(String emailCliente, List<LineaOrdenRequest> lineas) {

        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + emailCliente));

        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setEstado("PENDIENTE");

        List<DetalleOrden> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (LineaOrdenRequest linea : lineas) {
            Producto producto = productoRepository.findById(linea.getProductoCodigo())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + linea.getProductoCodigo()));

            DetalleOrden det = new DetalleOrden();
            det.setOrden(orden);
            det.setProducto(producto);
            det.setCantidad(linea.getCantidad());
            det.setPrecioUnitario(producto.getPrecio());
            BigDecimal subtotal = det.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(det.getCantidad()));

            total = total.add(subtotal);
            detalles.add(det);
        }

        orden.setDetalles(detalles);
        orden.setTotal(total);

        return ordenRepository.save(orden);
    }

    // Clase auxiliar para crear líneas de orden desde el controlador
    public static class LineaOrdenRequest {
        private Integer productoCodigo;
        private Integer cantidad;

        public Integer getProductoCodigo() {
            return productoCodigo;
        }

        public void setProductoCodigo(Integer productoCodigo) {
            this.productoCodigo = productoCodigo;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}
