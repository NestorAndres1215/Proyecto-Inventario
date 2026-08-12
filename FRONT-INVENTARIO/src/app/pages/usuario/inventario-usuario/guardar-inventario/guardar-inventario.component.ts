import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal, { SweetAlertIcon } from 'sweetalert2';
import { ProductoService } from 'src/app/core/services/producto.service';
import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { Producto } from 'src/app/core/models/producto';
import { AlertService } from 'src/app/core/services/alert.service';
import { firstValueFrom } from 'rxjs';

interface Proveedor {
  proveedorId: number;
  nombre?: string;
}

@Component({
  selector: 'app-guardar-producto',
  templateUrl: './guardar-inventario.component.html',
  styleUrls: ['./guardar-inventario.component.css'],
})
export class GuardarInventarioComponent implements OnInit {
  productoForm!: FormGroup;
  proveedores: Proveedor[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly productoService: ProductoService,
    private readonly proveedorService: ProveedorService,
    private readonly router: Router,
    private readonly alertService: AlertService,
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
      this.alertService.advertencia(
        'Campos incompletos',
        'Complete todos los campos requeridos.',
      );
      return;
    }
    try {
      const producto: Producto = this.productoForm.value;
      await firstValueFrom(this.productoService.agregarProducto(producto));
      this.alertService.aceptacion(
        'Registro exitoso',
        'El producto se registró correctamente.',
      );
      this.productoForm.reset();
      this.router.navigate(['/admin/producto']);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al registrar el producto.',
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
