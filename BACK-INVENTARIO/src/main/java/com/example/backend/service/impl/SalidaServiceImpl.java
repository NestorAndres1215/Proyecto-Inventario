import com.example.backend.constants.NotFoundMessages;
import com.example.backend.dto.request.SalidasRequest;
import com.example.backend.entity.DetalleSalida;
import com.example.backend.entity.Producto;
import com.example.backend.entity.Salidas;
import com.example.backend.entity.Usuario;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.Detalle_SalidaRepository;
import com.example.backend.repository.ProductoRepository;
import com.example.backend.repository.SalidaRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.SalidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalidaServiceImpl implements SalidaService {

    private final Detalle_SalidaRepository detalleSalidaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalidaRepository salidaRepository;

    @Override
    public List<DetalleSalida> crearDetalleSalida(List<SalidasRequest> detalles) {

        validarLista(detalles);

        Usuario usuario = obtenerUsuario(detalles.get(0).getUsuario());

        Salidas salida = crearSalida(detalles.get(0), usuario);

        List<DetalleSalida> detalleSalidas = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (SalidasRequest dto : detalles) {

            Producto producto = obtenerProducto(dto.getProducto());

            DetalleSalida detalle = crearDetalle(dto, producto, usuario, salida);

            total = total.add(detalle.getSubtotal());

            actualizarStock(producto, detalle.getStockActual());

            detalleSalidas.add(detalle);
        }

        detalleSalidaRepository.saveAll(detalleSalidas);

        salida.setTotal(total);
        salidaRepository.save(salida);

        return detalleSalidas;
    }

    private void validarLista(List<SalidasRequest> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("La lista de detalles no puede estar vacía.");
        }
    }

    private Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.USUARIO_NO_ENCONTRADO));
    }

    private Producto obtenerProducto(String nombre) {
        return productoRepository.findByNombre(nombre)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado: " + nombre));
    }

    private Salidas crearSalida(SalidasRequest dto, Usuario usuario) {

        Salidas salida = Salidas.builder()
                .usuario(usuario)
                .observacion(dto.getObservaciones())
                .fechaSalida(dto.getFechaSalida())
                .estado("REGISTRADO")
                .total(BigDecimal.ZERO)
                .build();

        return salidaRepository.save(salida);
    }

    private DetalleSalida crearDetalle(SalidasRequest dto, Producto producto, Usuario usuario, Salidas salida) {

        int stockAnterior = producto.getStock();
        int stockActual = stockAnterior - dto.getCantidad();

        if (stockActual < 0) {
            throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(dto.getCantidad()));

        return DetalleSalida.builder()
                .cantidad(dto.getCantidad())
                .descripcion(dto.getDescripcion())
                .precioUnitario(producto.getPrecio())
                .subtotal(subtotal)
                .stockAnterior(stockAnterior)
                .stockActual(stockActual)
                .usuario(usuario)
                .producto(producto)
                .salida(salida)
                .build();
    }

    private void actualizarStock(Producto producto, int nuevoStock) {
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }

    @Override
    public DetalleSalida obtenerPorId(Long id) {
        return detalleSalidaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(NotFoundMessages.SALIDA_NO_ENCONTRADO));
    }

    @Override
    public List<DetalleSalida> obtenerTodas() {
        return detalleSalidaRepository.findAll();
    }
}