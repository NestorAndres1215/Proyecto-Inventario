import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal, { SweetAlertIcon } from 'sweetalert2';
import { ProductoService } from 'src/app/core/services/producto.service';
import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { Producto } from 'src/app/core/models/producto';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';

interface Proveedor {
  proveedorId: number;
  nombre?: string;
}

interface AlertMessage {
  icon: SweetAlertIcon;
  title: string;
  text: string;
}

const ALERT_MESSAGES: {
  missingFields: AlertMessage;
  updateSuccess: AlertMessage;
  updateError: AlertMessage;
} = {
  missingFields: {
    icon: 'error',
    title: 'Faltan datos',
    text: 'Complete todos los campos correctamente antes de actualizar.',
  },
  updateSuccess: {
    icon: 'success',
    title: 'Producto actualizado',
    text: 'El producto se ha actualizado correctamente.',
  },
  updateError: {
    icon: 'error',
    title: 'Error al actualizar',
    text: 'Ocurrió un error al actualizar el producto.',
  },
};

// Mensajes constantes

@Component({
  selector: 'app-actualizar-producto',
  templateUrl: './actualizar-producto.component.html',
  styleUrls: ['./actualizar-producto.component.css'],
})
export class ActualizarProductoComponent implements OnInit {
  productoForm!: FormGroup;
  productoId: number = 0;
  proveedores: Proveedor[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly alertService : AlertService,
    private readonly productoService: ProductoService,
    private readonly proveedorService: ProveedorService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.productoId = Number(this.route.snapshot.paramMap.get('productoId'));
    this.inicializarFormulario();
    this.cargarProveedores();
    this.cargarProducto();
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
    }
  }

  private async cargarProducto(): Promise<void> {
    try {
      const producto = await firstValueFrom(
        this.productoService.obtenerProductoPorId(this.productoId),
      );

      this.productoForm.patchValue({
        nombre: producto.nombre,
        descripcion: producto.descripcion,
        precio: producto.precio,
        stock: producto.stock,
        ubicacion: producto.ubicacion,
        proveedorId: producto.proveedorId,
      });
    } catch (error) {
      console.error('Error al cargar producto:', error);
    }
  }

  async actualizarProducto(): Promise<void> {
    if (this.productoForm.invalid) {
      this.alertService.advertencia(
        'Campos incompletos',
        'Complete todos los campos requeridos.',
      );
      return;
    }

    const producto: Producto = this.productoForm.value;

    try {
      await firstValueFrom(this.productoService.actualizarProducto(producto));

      this.alertService.aceptacion(
        'Actualización exitosa',
        'El producto se actualizó correctamente.',
      );

      await this.router.navigate(['/admin/producto']);
    } catch (error: any) {
      console.error('Error al actualizar producto:', error);

      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al actualizar el producto.',
      );
    }
  }

  validarNumeroPositivo(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9]/g, '');
  }
}
