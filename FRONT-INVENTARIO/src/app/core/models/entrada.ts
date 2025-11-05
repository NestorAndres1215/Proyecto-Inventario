// Mensajes constantes
export const ALERT_MESSAGES = {
  fillFields: 'Complete todos los campos antes de agregar.',
  noRecords: 'Agregue al menos un registro antes de enviar.',
  sendSuccess: 'La entrada se ha enviado correctamente.',
  sendError: 'Hubo un problema al enviar la entrada.',
};

 export interface Producto {
  productoId: number;
  nombre?: string;
}

export interface Usuario {
  id: number;
  nombre?: string;
}

export interface DetalleEntrada {
  producto: { productoId: number };
  descripcion: string;
  cantidad: number;
  usuario: { id: number };
  entrada: { fechaEntrada: string };
}