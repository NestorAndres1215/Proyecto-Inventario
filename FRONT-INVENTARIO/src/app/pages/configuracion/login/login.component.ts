import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../../../core/services/login.service';

// Constantes
const SNACK_DURATION = 3000;
const MESSAGES = {
  requiredUsername: 'El nombre de usuario es requerido.',
  requiredPassword: 'La contraseña es requerida.',
  invalidCredentials: 'Detalles inválidos. Vuelva a intentar.',
};
const ROUTES = {
  admin: 'admin',
  user: 'user-dashboard',
};

// Enum para roles
enum UserRole {
  ADMIN = 'ADMIN',
  NORMAL = 'NORMAL',
}

// Interfaz para datos de login
interface LoginData {
  login: string;
  password: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginData: LoginData = { login: '', password: '' };
  hidePassword = true;

  constructor(
    private readonly snack: MatSnackBar,
    private readonly loginService: LoginService,
    private readonly router: Router
  ) { }

  ngOnInit(): void { }

  formSubmit(): void {
    const { login, password } = this.loginData;

    // Validación de campos vacíos
    if (!login?.trim()) {
      this.showSnack(MESSAGES.requiredUsername);
      return;
    }

    if (!password?.trim()) {
      this.showSnack(MESSAGES.requiredPassword);
      return;
    }

    // Petición para generar token
    this.loginService.generateToken(this.loginData).subscribe({
      next: (data: any) => this.handleLoginSuccess(data),
      error: () => this.showSnack(MESSAGES.invalidCredentials),
    });
  }

  // Manejo del login exitoso
  private handleLoginSuccess(data: any): void {
    this.loginService.loginUser(data.token);

    this.loginService.getCurrentUser().subscribe({
      next: (user: any) => {
        this.loginService.setUser(user);
        const rol = user.rol.nombre
        this.navigateByRole(rol);
      },
      error: () => this.loginService.logout(),
    });
  }

  // Navegación según rol
  private navigateByRole(role: string): void {
    switch (role) {
      case UserRole.ADMIN:
        this.router.navigate([ROUTES.admin]);
        break;
      case UserRole.NORMAL:
        this.router.navigate([ROUTES.user]);
        break;
      default:
        this.loginService.logout();
        return;
    }

    this.loginService.loginStatusSubjec.next(true);
  }

  // Mostrar mensaje de SnackBar
  private showSnack(message: string): void {
    this.snack.open(message, 'Aceptar', { duration: SNACK_DURATION });
  }
}
