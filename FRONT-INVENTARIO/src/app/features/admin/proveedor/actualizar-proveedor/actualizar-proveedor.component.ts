import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { AlertService } from 'src/app/core/services/alert.service';

@Component({
  selector: 'app-actualizar-proveedor',
  templateUrl: './actualizar-proveedor.component.html',
  styleUrls: ['./actualizar-proveedor.component.css'],
})
export class ActualizarProveedorComponent implements OnInit {
  proveedorForm!: FormGroup;
  proveedorId: number = 0;

  constructor(
    private fb: FormBuilder,
    private proveedorService: ProveedorService,
    private alertService: AlertService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.proveedorId = this.route.snapshot.params['proveedorId'];

    this.proveedorForm = this.fb.group({
      nombre: ['', Validators.required],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
      direccion: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      ruc: ['', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]],
    });

    this.cargarProveedor();
  }

  private async cargarProveedor(): Promise<void> {
    try {
      const proveedor = await firstValueFrom(
        this.proveedorService.obtenerProveedorPorId(this.proveedorId),
      );

      this.proveedorForm.patchValue(proveedor);
    } catch (error) {
      console.error('Error al cargar proveedor:', error);
    }
  }

  async actualizarProveedor(): Promise<void> {
    if (this.proveedorForm.invalid) {
      this.alertService.advertencia(
        'Campos incompletos',
        'Por favor, complete todos los campos correctamente.',
      );

      this.proveedorForm.markAllAsTouched();
      return;
    }

    try {
      await firstValueFrom(
        this.proveedorService.actualizarProveedor(
          this.proveedorId,
          this.proveedorForm.value,
        ),
      );

      this.alertService.aceptacion(
        'Proveedor actualizado',
        'El proveedor se ha actualizado correctamente.',
      );

      await this.router.navigate(['/admin/proveedor']);
    } catch (error: any) {
      console.error('Error al actualizar el proveedor:', error);

      this.alertService.error(
        'Error al actualizar',
        error.error?.message ?? 'Ocurrió un error al actualizar el proveedor.',
      );
    }
  }

  limitarLongitud(event: any, maxLength: number): void {
    const input = event.target;

    if (input.value.length > maxLength) {
      input.value = input.value.slice(0, maxLength);
    }
  }

  validarNumeroPositivo(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9]/g, '');
  }
}
