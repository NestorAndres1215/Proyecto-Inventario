import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { AlertService } from 'src/app/core/services/alert.service';
import { Proveedor } from 'src/app/core/models/proveedor';

@Component({
  selector: 'app-crear-proveedor',
  templateUrl: './crear-proveedor.component.html',
  styleUrls: ['./crear-proveedor.component.css'],
})
export class CrearProveedorComponent implements OnInit {
  proveedorForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private alertService: AlertService,
    private proveedorService: ProveedorService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.proveedorForm = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
      direccion: ['', Validators.required],
      ruc: ['', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]],
    });
  }

  async crearProveedor(): Promise<void> {
    if (this.proveedorForm.invalid) {
      this.alertService.advertencia(
        'Campos incompletos',
        'Por favor, complete todos los campos obligatorios.',
      );

      this.proveedorForm.markAllAsTouched();
      return;
    }

    const proveedor: Proveedor = this.proveedorForm.value;

    try {
      await firstValueFrom(this.proveedorService.agregarProveedor(proveedor));

      this.alertService.aceptacion(
        'Registro exitoso',
        'El proveedor se registró correctamente.',
      );

      await this.router.navigate(['/admin/proveedor']);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al registrar el proveedor.',
      );
    }
  }
}
