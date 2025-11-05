import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import baserUrl from 'src/app/core/models/helper';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { API_ENDPOINTS } from 'src/app/core/constants/api-endpoints';
import { ProductoValidator } from 'src/app/core/validator/producto.validator';
import { Producto } from '../models/producto';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  constructor(private http: HttpClient) { }

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
  agregarProducto(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(`${baserUrl}${API_ENDPOINTS.productos.base}/`, producto)
  }

  /** ========================
   *  ACTUALIZAR PRODUCTO
   * ======================== */
  actualizarProducto(producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${baserUrl}${API_ENDPOINTS.productos.actualizar}/`, producto)
  }

  /** ========================
   *  ACTIVAR / DESACTIVAR
   * ======================== */
  desactivarProducto(productoId: number): Observable<any> {
    return this.http.post(`${baserUrl}${API_ENDPOINTS.productos.desactivar}/${productoId}`, {});
  }

  activarProducto(productoId: number): Observable<any> {
    return this.http.post(`${baserUrl}${API_ENDPOINTS.productos.activar}/${productoId}`, {});
  }

}