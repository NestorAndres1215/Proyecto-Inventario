import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import baserUrl from 'src/app/core/models/helper';
import { API_ENDPOINTS } from 'src/app/core/constants/api-endpoints';
import { ProveedorValidator } from 'src/app/core/validator/proveedor.validator';

@Injectable({
  providedIn: 'root'
})
export class ProveedorService {
  constructor(private http: HttpClient) {}

  /** ========================
   *  LISTAR PROVEEDORES
   * ======================== */
  listarProveedoresActivos(): Observable<any[]> {
    return this.http.get<any[]>(`${baserUrl}${API_ENDPOINTS.proveedores.activados}`);
  }

  listarProveedoresDesactivados(): Observable<any[]> {
    return this.http.get<any[]>(`${baserUrl}${API_ENDPOINTS.proveedores.desactivados}`);
  }

  /** ========================
   *  CREAR NUEVO PROVEEDOR
   * ======================== */
  agregarProveedor(proveedor: any, ruc: any, direccion: any, telefono: any, email: any): Observable<any> {
    if (!ProveedorValidator.esProveedorValido(proveedor)) {
      throw new Error('Datos de proveedor inválidos');
    }

    const formData = this.crearFormData(proveedor);
    const headers = new HttpHeaders({ enctype: 'multipart/form-data' });

    return this.http.post<any>(`${baserUrl}${API_ENDPOINTS.proveedores.base}/`, formData, { headers });
  }

  /** ========================
   *  ACTUALIZAR PROVEEDOR
   * ======================== */
  actualizarProveedor(proveedorId: number, proveedor: any): Observable<any> {
    if (!ProveedorValidator.esProveedorValido(proveedor)) {
      throw new Error('Datos de proveedor inválidos');
    }

    const formData = this.crearFormData(proveedor);
    const headers = new HttpHeaders({ enctype: 'multipart/form-data' });

    return this.http.put(`${baserUrl}${API_ENDPOINTS.proveedores.base}/${proveedorId}`, formData, { headers });
  }

  /** ========================
   *  OBTENER PROVEEDOR POR ID
   * ======================== */
  obtenerProveedorPorId(proveedorId: number): Observable<any> {
    return this.http.get(`${baserUrl}${API_ENDPOINTS.proveedores.base}/${proveedorId}`);
  }

  /** ========================
   *  ACTIVAR / DESACTIVAR
   * ======================== */
  desactivarProveedor(proveedorId: number): Observable<any> {
    return this.http.post(`${baserUrl}${API_ENDPOINTS.proveedores.desactivar}/${proveedorId}`, {});
  }

  activarProveedor(proveedorId: number): Observable<any> {
    return this.http.post(`${baserUrl}${API_ENDPOINTS.proveedores.activar}/${proveedorId}`, {});
  }

  /** ========================
   *  MÉTODO PRIVADO UTILITARIO
   * ======================== */
  private crearFormData(proveedor: any): FormData {
    const formData = new FormData();
    formData.append('nombre', proveedor.nombre);
    formData.append('ruc', proveedor.ruc);
    formData.append('direccion', proveedor.direccion);
    formData.append('telefono', proveedor.telefono);
    formData.append('email', proveedor.email);
    return formData;
  }
}
