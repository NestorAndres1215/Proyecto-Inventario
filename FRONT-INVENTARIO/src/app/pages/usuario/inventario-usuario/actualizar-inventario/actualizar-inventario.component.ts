import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Producto } from 'src/app/core/models/producto';
import { AlertService } from 'src/app/core/services/alert.service';
import { ProductoService } from 'src/app/core/services/producto.service';
import { ProveedorService } from 'src/app/core/services/proveedor.service';

interface Proveedor {
  proveedorId: number;
  nombre?: string;
}

@Component({
  selector: 'app-actualizar-inventario',
  templateUrl: './actualizar-inventario.component.html',
  styleUrls: ['./actualizar-inventario.component.css'],
})
export class ActualizarInventarioComponent implements OnInit {
  productoForm!: FormGroup;
  productoId = 0;
  proveedores: Proveedor[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly productoService: ProductoService,
    private readonly proveedorService: ProveedorService,
    private readonly alertService: AlertService,
    private readonly router: Router,
  ) {}

  async ngOnInit(): Promise<void> {
    this.productoId = Number(this.route.snapshot.paramMap.get('productoId'));

    this.inicializarFormulario();

    await Promise.all([this.cargarProveedores(), this.cargarProducto()]);
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

  private async cargarProveedores(): Promise<void> {
    try {
      this.proveedores = await firstValueFrom(
        this.proveedorService.listarProveedoresActivos(),
      );
    } catch (error) {
      console.error('Error al cargar proveedores:', error);
      this.alertService.error(
        'Error',
        'No se pudieron cargar los proveedores.',
      );
    }
  }

  private async cargarProducto(): Promise<void> {
    try {
      const producto = await firstValueFrom(
        this.productoService.obtenerProductoPorId(this.productoId),
      );

      this.productoForm.patchValue(producto);
    } catch (error) {
      console.error('Error al cargar producto:', error);
      this.alertService.error('Error', 'No se pudo cargar el producto.');
    }
  }

  async actualizarProducto(): Promise<void> {
    if (this.productoForm.invalid) {
      this.alertService.advertencia(
        'Campos obligatorios',
        'Complete todos los campos requeridos.',
      );
      return;
    }

    try {
      const producto: Producto = {
        ...this.productoForm.value,
        productoId: this.productoId,
      };

      await firstValueFrom(this.productoService.actualizarProducto(producto));

      this.alertService.aceptacion(
        'Producto actualizado',
        'El producto se actualizó correctamente.',
      );

      this.router.navigate(['/admin/producto']);
    } catch (error) {
      console.error('Error al actualizar producto:', error);

      this.alertService.error(
        'Error al actualizar',
        'Ocurrió un error al actualizar el producto.',
      );
    }
  }

  validarNumeroPositivo(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9]/g, '');
  }
}
