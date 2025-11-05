import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal, { SweetAlertIcon } from 'sweetalert2';
import { ProductoService } from 'src/app/core/services/producto.service';
import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { Producto } from 'src/app/core/models/producto';

interface Proveedor {
  proveedorId: number;
  nombre?: string;
}

const ALERT_MESSAGES = {
  missingFields: { icon: 'error' as SweetAlertIcon, title: 'Faltan datos', text: 'Complete los campos.' },
  saveSuccess: { icon: 'success' as SweetAlertIcon, title: 'Producto guardado', text: 'Se registró correctamente.' },
  saveError: { icon: 'error' as SweetAlertIcon, title: 'Error', text: 'No se pudo registrar.' },
  updateSuccess: { icon: 'success' as SweetAlertIcon, title: 'Producto actualizado', text: 'Se actualizó correctamente.' },
  updateError: { icon: 'error' as SweetAlertIcon, title: 'Error', text: 'No se pudo actualizar.' }
};


@Component({
  selector: 'app-crear-producto',
  templateUrl: './crear-producto.component.html',
  styleUrls: ['./crear-producto.component.css']
})
export class CrearProductoComponent implements OnInit {

  productoForm!: FormGroup;
  proveedores: Proveedor[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly productoService: ProductoService,
    private readonly proveedorService: ProveedorService,
    private readonly router: Router
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
      proveedorId: ['', Validators.required]
    });
  }

 formSubmit(): void {
  if (this.productoForm.invalid) {
    Swal.fire(ALERT_MESSAGES.missingFields);
    return;
  }

  const producto: Producto = this.productoForm.value;

  this.productoService.agregarProducto(producto)
    .subscribe({
      next: () => {
        Swal.fire(ALERT_MESSAGES.saveSuccess).then(() => {
          this.productoForm.reset();
          this.router.navigate(['/admin/producto']);
        });
      },
      error: (err) => {
        console.error('Error al guardar producto:', err);
        Swal.fire(ALERT_MESSAGES.saveError);
      }
    });
}


  private obtenerProveedores(): void {
    this.proveedorService.listarProveedoresActivos().subscribe({
      next: (data: Proveedor[]) => this.proveedores = data,
      error: (err) => console.error('Error al obtener proveedores:', err)
    });
  }
}
