export interface DetalleEntrada {
  descripcion: string;
  cantidad: number | null;
  producto: {
    productoId: string;
    nombre?: string;
  };
  usuario: {
    id: string;
    username?: string;
  };
  entrada: {
    fechaEntrada: string;
  };
}
