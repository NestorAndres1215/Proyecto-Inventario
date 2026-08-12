import { Component, OnInit } from '@angular/core';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Usuario } from 'src/app/core/models/usuario';

import { AlertService } from 'src/app/core/services/alert.service';
import { firstValueFrom } from 'rxjs';
@Component({
  selector: 'app-registrar-usuario-administrador',
  templateUrl: './registrar-usuario-administrador.component.html',
  styleUrls: ['./registrar-usuario-administrador.component.css'],
})
export class RegistrarUsuarioAdministradorComponent implements OnInit {
  form!: FormGroup;

  constructor(
    private alertService: AlertService,
    private fb: FormBuilder,
    private router: Router,
    private userService: UsuarioService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.maxLength(8)]],
      dni: ['', [Validators.required, Validators.maxLength(8)]],
      direccion: ['', Validators.required],
      fechaNacimiento: [''],
      edad: [''],
    });
  }

  async formSubmit(): Promise<void> {
    if (this.form.invalid) {
      this.alertService.advertencia(
        'Campos incompletos',
        'Por favor, complete todos los campos obligatorios.',
      );

      this.form.markAllAsTouched();
      return;
    }

    const user: Usuario = {
      username: 'O' + this.form.value.dni,
      password: this.form.value.dni + 'O',
      nombre: this.form.value.nombre,
      apellido: this.form.value.apellido,
      email: this.form.value.email,
      telefono: this.form.value.telefono,
      dni: this.form.value.dni,
      direccion: this.form.value.direccion,
      fechaNacimiento: this.form.value.fechaNacimiento,
      edad: this.form.value.edad,
      rol: 'ADMIN',
    };

    try {
      await firstValueFrom(this.userService.registrarAdmin(user));

      this.alertService.aceptacion(
        'Registro exitoso',
        'El usuario se registró correctamente.',
      );

      this.router.navigate(['/admin/usuario/admin']);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al registrar el usuario.',
      );
    }
  }
}
