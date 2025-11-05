import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import baserUrl from 'src/app/core/models/helper';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { UsuarioValidator } from '../validator/usuario.validator';
import { Usuario } from '../models/usuario';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient) {}

  /** ========================
   *  USUARIOS ADMIN ACTIVADOS
   * ======================== */
  obtenerAdminUsuariosActivos(): Observable<any> {
    return this.http.get(`${baserUrl}${API_ENDPOINTS.usuarios.adminActivos}`);
  }

  /** ========================
   *  USUARIOS NORMAL ACTIVADOS
   * ======================== */
  obtenerUsuariosNormalesActivos(): Observable<any> {
    return this.http.get(`${baserUrl}${API_ENDPOINTS.usuarios.normalActivos}`);
  }

  /** =========================
   *  USUARIOS ADMIN DESACTIVADOS
   * ========================= */
  obtenerAdminUsuariosDesactivados(): Observable<any> {
    return this.http.get(`${baserUrl}${API_ENDPOINTS.usuarios.adminDesactivados}`);
  }

  /** =========================
   *  USUARIOS NORMAL DESACTIVADOS
   * ========================= */
  obtenerUsuariosNormalesDesactivados(): Observable<any> {
    return this.http.get(`${baserUrl}${API_ENDPOINTS.usuarios.normalDesactivados}`);
  }

  /** ========================
   *  REGISTRO DE USUARIOS
   * ======================== */
  registrarAdmin(user: any): Observable<any> {
    if (!UsuarioValidator.esUsuarioValido(user)) {
      throw new Error('Datos de usuario inválidos');
    }
    return this.http.post(`${baserUrl}${API_ENDPOINTS.usuarios.registrarAdmin}`, user);
  }

  registrarNormal(user: any): Observable<any> {
    if (!UsuarioValidator.esUsuarioValido(user)) {
      throw new Error('Datos de usuario inválidos');
    }
    return this.http.post(`${baserUrl}${API_ENDPOINTS.usuarios.registrarNormal}`, user);
  }

  /** ========================
   *  ACTIVAR / DESACTIVAR
   * ======================== */
  desactivarUsuario(usuarioRolId: number): Observable<string> {
    return this.http.put(
      `${baserUrl}${API_ENDPOINTS.usuarios.desactivar}/${usuarioRolId}`,
      null,
      { responseType: 'text' }
    );
  }

  activarUsuario(usuarioRolId: number): Observable<string> {
    return this.http.put(
      `${baserUrl}${API_ENDPOINTS.usuarios.activar}/${usuarioRolId}`,
      null,
      { responseType: 'text' }
    );
  }

  /** ========================
   *  CRUD ADICIONAL
   * ======================== */
  obtenerUsuarioPorId(usuarioRolId: number): Observable<any> {
    return this.http.get(`${baserUrl}${API_ENDPOINTS.usuarios.obtenerPorId}/${usuarioRolId}`);
  }

  actualizarUsuario(id: number, usuarioActualizado: any): Observable<any> {
    if (!UsuarioValidator.esUsuarioValido(usuarioActualizado)) {
      throw new Error('Datos de usuario inválidos');
    }
    return this.http.put(`${baserUrl}${API_ENDPOINTS.usuarios.actualizar}/${id}`, usuarioActualizado);
  }
}
