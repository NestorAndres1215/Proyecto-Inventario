import Swal from 'sweetalert2';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Usuario } from 'src/app/core/models/usuario';

@Component({
  selector: 'app-registrar-usuario-operador',
  templateUrl: './registrar-usuario-operador.component.html',
  styleUrls: ['./registrar-usuario-operador.component.css'],
})
export class RegistrarUsuarioOperadorComponent implements OnInit {
  form!: FormGroup;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UsuarioService,
    private snack: MatSnackBar
  ) { }

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
      edad: ['']
    });
  }


  formSubmit() {
    if (this.form.invalid) {
      Swal.fire({
        icon: 'error',
        title: 'Faltan datos',
        text: 'Por favor, ingrese todos los campos requeridos correctamente.'
      });
      return;
    }

    const user: Usuario = {
      username: 'O' + this.form.value.dni, // Generado automáticamente
      password: this.form.value.dni + 'O', // Generado automáticamente
      nombre: this.form.value.nombre,
      apellido: this.form.value.apellido,
      email: this.form.value.email,
      telefono: this.form.value.telefono,
      dni: this.form.value.dni,
      direccion: this.form.value.direccion,
      fechaNacimiento: this.form.value.fechaNacimiento,
      edad: this.form.value.edad
    };
    this.userService.registrarNormal(user).subscribe({
      next: (data) => {
        Swal.fire('Usuario guardado', 'Usuario registrado con éxito en el sistema', 'success');
        this.router.navigate(['/admin/usuario']);
      },
      error: (err) => {
        console.error(err);
        this.snack.open('Ha ocurrido un error en el sistema', 'Aceptar', { duration: 3000 });
      },
      complete: () => {
        console.log('Solicitud completada');
      }
    });

  }
  limitarLongitud1(event: any) {
    const input = event.target;
    const maxLength = 8; // Máxima longitud permitida

    if (input.value.length > maxLength) {
      input.value = input.value.slice(0, maxLength); // Limita la longitud
    }
  }
  limitarLongitudTelefono(event: any) {
    const input = event.target;
    const maxLength = 9; // Máxima longitud permitida

    if (input.value.length > maxLength) {
      input.value = input.value.slice(0, maxLength); // Limita la longitud
    }
  }
}
