import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import baserUrl from '../models/helper';
import { API_ENDPOINTS } from 'src/app/core/constants/api-endpoints';
import { AuthValidator } from 'src/app/core/validator/auth.validator';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private readonly TOKEN_KEY = 'token';
  private readonly USER_KEY = 'user';
  public loginStatusSubject = new Subject<boolean>();

  constructor(private http: HttpClient) {}

  /** ========================
   *  AUTENTICACIÓN
   * ======================== */
  generateToken(loginData: any): Observable<any> {
    if (!AuthValidator.isLoginDataValid(loginData)) {
      return throwError(() => new Error('Datos de inicio de sesión inválidos.'));
    }

    return this.http
      .post(`${baserUrl}${API_ENDPOINTS.auth.generateToken}`, loginData)
      .pipe(catchError(this.handleError));
  }

  getCurrentUser(): Observable<any> {
    return this.http
      .get(`${baserUrl}${API_ENDPOINTS.auth.currentUser}`)
      .pipe(catchError(this.handleError));
  }

  /** ========================
   *  SESIÓN LOCAL
   * ======================== */
  loginUser(token: string): boolean {
    localStorage.setItem(this.TOKEN_KEY, token);
    return true;
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem(this.TOKEN_KEY);
    return !!token && token.trim() !== '';
  }

  logout(): boolean {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    return true;
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setUser(user: any): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  getUser(): any | null {
    const userStr = localStorage.getItem(this.USER_KEY);
    if (userStr) {
      return JSON.parse(userStr);
    } else {
      this.logout();
      return null;
    }
  }

  getUserRole(): string | null {
    const user = this.getUser();
    return user?.authorities?.[0]?.authority || null;
  }

  /** ========================
   *  MANEJO DE ERRORES
   * ======================== */
  private handleError(error: any) {
    console.error('Error en el servicio de Login:', error);
    return throwError(() => new Error('Error en la autenticación.'));
  }
}
