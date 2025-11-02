import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import baserUrl from 'src/app/core/models/helper';
import { API_ENDPOINTS } from 'src/app/core/constants/api-endpoints';
import { EntradaValidator } from 'src/app/core/validator/entrada.validator';

@Injectable({
  providedIn: 'root'
})
export class EntradaService {
  constructor(private http: HttpClient) {}

  /** ========================
   *  LISTAR ENTRADAS
   * ======================== */
  listarEntradas(): Observable<any[]> {
    return this.http
      .get<any[]>(`${baserUrl}${API_ENDPOINTS.entradas.base}`)
      .pipe(catchError(this.handleError));
  }

  /** ========================
   *  CREAR ENTRADA CON DETALLES
   * ======================== */
  crearEntradaConDetalles(listaDetalleEntrada: any[]): Observable<any> {
    if (!EntradaValidator.esListaValida(listaDetalleEntrada)) {
      return throwError(() => new Error('Lista de detalles de entrada inválida.'));
    }

    return this.http
      .post(`${baserUrl}${API_ENDPOINTS.entradas.base}`, listaDetalleEntrada)
      .pipe(catchError(this.handleError));
  }

  /** ========================
   *  ACTUALIZAR DETALLE DE ENTRADA
   * ======================== */
  actualizarDetalleEntrada(detalleEntradaId: number, detalleEntrada: any): Observable<any> {
    if (!EntradaValidator.esDetalleValido(detalleEntrada)) {
      return throwError(() => new Error('Datos del detalle de entrada inválidos.'));
    }

    const url = `${baserUrl}${API_ENDPOINTS.entradas.base}/${detalleEntradaId}`;
    return this.http
      .put(url, detalleEntrada)
      .pipe(catchError(this.handleError));
  }

  /** ========================
   *  OBTENER ENTRADA POR ID
   * ======================== */
  obtenerEntradaPorId(detalleEntradaId: number): Observable<any> {
    return this.http
      .get(`${baserUrl}${API_ENDPOINTS.entradas.base}/${detalleEntradaId}`)
      .pipe(catchError(this.handleError));
  }

  /** ========================
   *  MÉTODOS PRIVADOS
   * ======================== */
  private handleError(error: any) {
    console.error('Error en EntradaService:', error);
    return throwError(() => new Error('Error en la operación de entradas.'));
  }
}
