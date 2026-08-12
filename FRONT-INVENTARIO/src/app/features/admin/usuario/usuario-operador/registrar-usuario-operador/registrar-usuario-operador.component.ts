import Swal from 'sweetalert2';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Usuario } from 'src/app/core/models/usuario';
import { AlertService } from 'src/app/core/services/alert.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-registrar-usuario-operador',
  templateUrl: './registrar-usuario-operador.component.html',
  styleUrls: ['./registrar-usuario-operador.component.css'],
})
export class RegistrarUsuarioOperadorComponent implements OnInit {
  form!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private alertService: AlertService,
    private router: Router,
    private userService: UsuarioService,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required]],
      telefono: ['', [Validators.required]],
      dni: ['', [Validators.required]],
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
      rol: 'NORMAL',
    };

    try {
      await firstValueFrom(this.userService.registrarNormal(user));

      this.alertService.aceptacion(
        'Registro exitoso',
        'El usuario se registró correctamente.',
      );

      this.router.navigate(['/admin/usuario/operador']);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al registrar el usuario.',
      );
    }
  }
}
