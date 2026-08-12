import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { ProductoService } from 'src/app/core/services/producto.service';
import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { AlertService } from 'src/app/core/services/alert.service';
import { Producto } from 'src/app/core/models/producto';

interface Proveedor {
  proveedorId: number;
  nombre?: string;
}

@Component({
  selector: 'app-crear-producto',
  templateUrl: './crear-producto.component.html',
  styleUrls: ['./crear-producto.component.css'],
})
export class CrearProductoComponent implements OnInit {
  productoForm!: FormGroup;
  proveedores: Proveedor[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly productoService: ProductoService,
    private readonly proveedorService: ProveedorService,
    private readonly alertService: AlertService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
    this.obtenerProveedores();
  }

  private inicializarFormulario(): void {
    this.productoForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: ['', Validators.required],
      precio: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      stock: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      ubicacion: ['', Validators.required],
      proveedorId: ['', Validators.required],
    });
  }

  async formSubmit(): Promise<void> {
    if (this.productoForm.invalid) {
      this.alertService.advertencia('Faltan datos', 'Complete los campos.');

      this.productoForm.markAllAsTouched();
      return;
    }

    const producto: Producto = this.productoForm.value;

    try {
      await firstValueFrom(this.productoService.agregarProducto(producto));

      this.alertService.aceptacion(
        'Producto guardado',
        'Se registró correctamente.',
      );

      this.productoForm.reset();

      await this.router.navigate(['/admin/producto']);
    } catch (error: any) {
      console.error('Error al guardar producto:', error);

      this.alertService.error(
        'Error',
        error.error?.message ?? 'No se pudo registrar.',
      );
    }
  }

  private async obtenerProveedores(): Promise<void> {
    try {
      this.proveedores = await firstValueFrom(
        this.proveedorService.listarProveedoresActivos(),
      );
    } catch (error) {
      console.error('Error al obtener proveedores:', error);
    }
  }
}
