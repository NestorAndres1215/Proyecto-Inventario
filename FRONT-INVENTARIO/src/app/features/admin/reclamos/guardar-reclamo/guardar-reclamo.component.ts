import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { Reclamos } from 'src/app/core/models/reclamo';
import { AlertService } from 'src/app/core/services/alert.service';
import { LoginService } from 'src/app/core/services/login.service';
import { ReclamoService } from 'src/app/core/services/reclamo.service';

@Component({
  selector: 'app-guardar-reclamo',
  templateUrl: './guardar-reclamo.component.html',
  styleUrls: ['./guardar-reclamo.component.css'],
})
export class GuardarReclamoComponent implements OnInit {
  isLoggedIn = false;
  user: any = null;
  reclamoForm!: FormGroup;

  constructor(
    private readonly loginService: LoginService,
    private readonly router: Router,
    private readonly reclamoService: ReclamoService,
    private readonly alertService: AlertService,
    private readonly fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.obtenerUsuario();
    this.crearFormulario();
  }

  private crearFormulario(): void {
    this.reclamoForm = this.fb.group({
      asunto: ['', [Validators.required, Validators.minLength(10)]],
    });
  }

  async enviarEntrada(): Promise<void> {
    if (this.reclamoForm.invalid) {
      this.alertService.advertencia(
        'Campos incompletos',
        'El asunto es obligatorio y debe tener al menos 10 caracteres.',
      );

      this.reclamoForm.markAllAsTouched();
      return;
    }

    try {
      const reclamo: Reclamos = {
        asunto: this.reclamoForm.value.asunto,
        usuario: this.user,
      };

      await firstValueFrom(this.reclamoService.agregarReclamo(reclamo));

      this.alertService.aceptacion(
        'Éxito',
        'El reclamo se envió correctamente.',
      );

      this.reclamoForm.reset();
      this.router.navigate(['/user-dashboard/configuracion']);
    } catch (error) {
      console.error('Error al enviar el reclamo:', error);

      this.alertService.error(
        'Error',
        'Hubo un problema al enviar el reclamo.',
      );
    }
  }

  private obtenerUsuario(): void {
    this.cargarUsuario();

    this.loginService.loginStatusSubject.subscribe(() => {
      this.cargarUsuario();
    });
  }

  private cargarUsuario(): void {
    this.isLoggedIn = this.loginService.isLoggedIn();
    this.user = this.loginService.getUser();
  }
}
