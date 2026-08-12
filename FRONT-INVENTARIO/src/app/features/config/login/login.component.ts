import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../../../core/services/login.service';
import { ROLES } from 'src/app/core/constants/rol';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlertService } from 'src/app/core/services/alert.service';

import { LoginData } from 'src/app/core/constants/auth';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  hidePassword = true;
  formulario!: FormGroup;

  constructor(
    private loginService: LoginService,
    private alertService: AlertService,
    private fb: FormBuilder,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  verContraActual = false;

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }
  initForm() {
    this.formulario = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  async operar(): Promise<void> {
    if (this.formulario.invalid) {
      this.alertService.advertencia(
        'Campos incompletos',
        'Ingrese su usuario y contraseña.',
      );
      this.formulario.markAllAsTouched();
      return;
    }

    try {
      const login: LoginData = this.formulario.getRawValue();

      const data: any = await firstValueFrom(
        this.loginService.generateToken(login),
      );

      this.loginService.loginUser(data.token);

      const user: any = await firstValueFrom(
        this.loginService.getCurrentUser(),
      );

      this.loginService.setUser(user);
console.log(user)
      const rol = user.authorities?.[0]?.authority;
      console.log(rol)
      this.navigateByRole(rol);
    } catch (error: any) {
      console.error(error);

      this.alertService.error(
        'Inicio de sesión',
        error?.error?.message ,
      );

      this.loginService.logout();
    }
  }

  private navigateByRole(role: string): void {
    switch (role) {
      case ROLES.ADMIN:
        this.router.navigate(['admin']);
        break;
      case ROLES.NORMAL:
        this.router.navigate(['user-dashboard']);
        break;
      default:
        this.loginService.logout();
        return;
    }

    this.loginService.loginStatusSubject.next(true);
  }
}
