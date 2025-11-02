export class ProveedorValidator {
  static esProveedorValido(proveedor: any): boolean {
    if (!proveedor) return false;

    const { nombre, ruc, direccion, telefono, email } = proveedor;

    const nombreValido = typeof nombre === 'string' && nombre.trim().length >= 3;
    const rucValido = /^[0-9]{8,11}$/.test(ruc); // 8-11 dígitos
    const direccionValida = typeof direccion === 'string' && direccion.trim().length >= 5;
    const telefonoValido = /^[0-9]{7,9}$/.test(telefono);
    const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

    return nombreValido && rucValido && direccionValida && telefonoValido && emailValido;
  }
}
