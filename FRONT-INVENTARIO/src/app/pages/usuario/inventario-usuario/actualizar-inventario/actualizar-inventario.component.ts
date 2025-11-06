import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal, { SweetAlertIcon } from 'sweetalert2';
import { ProductoService } from 'src/app/core/services/producto.service';
import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { Producto } from 'src/app/core/models/producto';

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
    text: 'Complete todos los campos correctamente antes de actualizar.'
  },
  updateSuccess: {
    icon: 'success',
    title: 'Producto actualizado',
    text: 'El producto se ha actualizado correctamente.'
  },
  updateError: {
    icon: 'error',
    title: 'Error al actualizar',
    text: 'Ocurrió un error al actualizar el producto.'
  }
};


// Mensajes constantes

@Component({
  selector: 'app-actualizar-producto',
  templateUrl: './actualizar-producto.component.html',
  styleUrls: ['./actualizar-producto.component.css']
})
export class ActualizarInventarioComponent implements OnInit {

  productoForm!: FormGroup;
  productoId: number = 0;
  proveedores: Proveedor[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly productoService: ProductoService,
    private readonly proveedorService: ProveedorService,
    private readonly router: Router
  ) { }

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
      proveedorId: ['', Validators.required]
    });
  }

  private cargarProveedores(): void {
    this.proveedorService.listarProveedoresActivos().subscribe({
      next: (data: Proveedor[]) => this.proveedores = data,
      error: (err) => console.error('Error al cargar proveedores:', err)
    });
  }

  private cargarProducto(): void {
    this.productoService.obtenerProductoPorId(this.productoId).subscribe({
      next: (producto: Producto) => {
        this.productoForm.patchValue({
          nombre: producto.nombre,
          descripcion: producto.descripcion,
          precio: producto.precio,
          stock: producto.stock,
          ubicacion: producto.ubicacion,
          proveedorId: producto.proveedorId
        });
      },
      error: (err) => console.error('Error al cargar producto:', err)
    });
  }

  actualizarProducto(): void {
  if (this.productoForm.invalid) {
    Swal.fire(ALERT_MESSAGES.missingFields);
    return;
  }

  const producto: Producto = this.productoForm.value; // Incluye id

  this.productoService.actualizarProducto(producto)
    .subscribe({
      next: () => {
        Swal.fire(ALERT_MESSAGES.updateSuccess)
          .then(() => this.router.navigate(['/admin/producto']));
      },
      error: (err) => {
        console.error('Error al actualizar producto:', err);
        Swal.fire(ALERT_MESSAGES.updateError);
      }
    });
}


  validarNumeroPositivo(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9]/g, '');
  }

}
