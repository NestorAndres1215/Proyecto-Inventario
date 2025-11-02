import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import baserUrl from 'src/app/core/models/helper';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { API_ENDPOINTS } from 'src/app/core/constants/api-endpoints';
import { ProductoValidator } from 'src/app/core/validator/producto.validator';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  constructor(private http: HttpClient) {}

  /** ========================
   *  LISTAR PRODUCTOS
   * ======================== */
  listarProductosActivos(): Observable<any[]> {
    return this.http.get<any[]>(`${baserUrl}${API_ENDPOINTS.productos.activados}`);
  }

  listarProductosDesactivados(): Observable<any[]> {
    return this.http.get<any[]>(`${baserUrl}${API_ENDPOINTS.productos.desactivados}`);
  }

  /** ========================
   *  OBTENER POR ID
   * ======================== */
  obtenerProductoPorId(productoId: number): Observable<any> {
    return this.http.get(`${baserUrl}${API_ENDPOINTS.productos.base}/${productoId}`);
  }

  /** ========================
   *  CREAR PRODUCTO
   * ======================== */
  agregarProducto(producto: any): Observable<any> {
    if (!ProductoValidator.esProductoValido(producto)) {
      return throwError(() => new Error('Datos del producto inválidos.'));
    }

    const formData = this.crearFormData(producto);
    const headers = new HttpHeaders({ enctype: 'multipart/form-data' });

    return this.http
      .post<any>(`${baserUrl}${API_ENDPOINTS.productos.base}/`, formData, { headers })
      .pipe(catchError(this.handleError));
  }

  /** ========================
   *  ACTUALIZAR PRODUCTO
   * ======================== */
  actualizarProducto(productoId: number, producto: any): Observable<any> {
    if (!ProductoValidator.esProductoValido(producto)) {
      return throwError(() => new Error('Datos del producto inválidos.'));
    }

    const formData = this.crearFormData(producto);
    const headers = new HttpHeaders({ enctype: 'multipart/form-data' });

    return this.http
      .put(`${baserUrl}${API_ENDPOINTS.productos.base}/${productoId}`, formData, { headers })
      .pipe(catchError(this.handleError));
  }

  /** ========================
   *  ACTIVAR / DESACTIVAR
   * ======================== */
  desactivarProducto(productoId: number): Observable<any> {
    return this.http
      .post(`${baserUrl}${API_ENDPOINTS.productos.desactivar}/${productoId}`, {})
      .pipe(catchError(this.handleError));
  }

  activarProducto(productoId: number): Observable<any> {
    return this.http
      .post(`${baserUrl}${API_ENDPOINTS.productos.activar}/${productoId}`, {})
      .pipe(catchError(this.handleError));
  }

  /** ========================
   *  MÉTODOS PRIVADOS
   * ======================== */
  private crearFormData(producto: any): FormData {
    const formData = new FormData();
    formData.append('nombre', producto.nombre);
    formData.append('descripcion', producto.descripcion);
    formData.append('precio', producto.precio.toString());
    formData.append('stock', producto.stock.toString());
    formData.append('ubicacion', producto.ubicacion);
    formData.append('proveedorId', producto.proveedor?.proveedorId?.toString() || '');
    return formData;
  }

  private handleError(error: any) {
    console.error('Error en la solicitud HTTP:', error);
    return throwError(() => new Error('Error en el servicio de producto.'));
  }
}
