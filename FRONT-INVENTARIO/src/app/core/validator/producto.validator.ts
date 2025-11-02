export class ProductoValidator {
  static esProductoValido(producto: any): boolean {
    if (!producto) return false;

    const { nombre, descripcion, precio, stock, ubicacion, proveedor } = producto;

    const nombreValido = typeof nombre === 'string' && nombre.trim().length >= 3;
    const descripcionValida = typeof descripcion === 'string' && descripcion.trim().length >= 5;
    const precioValido = !isNaN(precio) && Number(precio) > 0;
    const stockValido = Number.isInteger(Number(stock)) && Number(stock) >= 0;
    const ubicacionValida = typeof ubicacion === 'string' && ubicacion.trim().length >= 3;
    const proveedorValido = proveedor && Number(proveedor.proveedorId) > 0;

    return nombreValido && descripcionValida && precioValido && stockValido && ubicacionValida && proveedorValido;
  }
}
