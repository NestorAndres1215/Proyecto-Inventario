import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DetalleSalida } from 'src/app/core/models/detalle-salidad';
import { LoginService } from 'src/app/core/services/login.service';
import { ProductoService } from 'src/app/core/services/producto.service';
import { SalidaService } from 'src/app/core/services/salida.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-registrar-salidas',
  templateUrl: './registrar-salidas.component.html',
  styleUrls: ['./registrar-salidas.component.css']
})
export class RegistrarSalidasComponent implements OnInit {

  salidaForm!: FormGroup;
  productos: any[] = [];
  listaDetalleSalida: DetalleSalida[] = [];
  user: any = null; 

  constructor(
    private fb: FormBuilder,
    private productoService: ProductoService,
    private loginService: LoginService,
    private salidaService: SalidaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.salidaForm = this.fb.group({
      productoId: ['', Validators.required],
      descripcion: ['', Validators.required],
      cantidad: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      fechaSalida: ['', Validators.required]
    });

    this.obtenerProducto();
    this.obtenerUsuario();
  }

  obtenerProducto(): void {
    this.productoService.listarProductosActivos().subscribe({
      next: (data) => this.productos = data,
      error: (err) => console.error('Error al obtener productos:', err)
    });
  }

  obtenerUsuario(): void {
    this.user = this.loginService.getUser();
  }

  agregarProducto(): void {
    if (this.salidaForm.invalid) {
      Swal.fire('Campos incompletos', 'Complete todos los campos antes de agregar.', 'warning');
      return;
    }

    const { productoId, descripcion, cantidad, fechaSalida } = this.salidaForm.value;

    // 1️⃣ Verificar si el producto ya fue agregado
    if (this.listaDetalleSalida.some(d => d.producto.productoId === productoId)) {
      Swal.fire('Advertencia', 'Este producto ya fue agregado en la salida.', 'warning');
      return;
    }

    const detalle: DetalleSalida = {
      producto: { productoId },
      descripcion,
      cantidad,
      salida: { fechaSalida },
      usuario: { id: this.user.id }
    };

    this.listaDetalleSalida.push(detalle);

    Swal.fire({
      icon: 'success',
      title: 'Producto agregado',
      timer: 1200,
      showConfirmButton: false
    });

    this.salidaForm.reset({ fechaSalida }); // Mantener la fecha para agregar más rápido
  }

  enviarSalida(): void {
    if (this.listaDetalleSalida.length === 0) {
      Swal.fire('Sin registros', 'Agregue al menos un producto antes de enviar.', 'warning');
      return;
    }

    this.salidaService.crearSalidaConDetalles(this.listaDetalleSalida)
      .subscribe({
        next: () => {
          Swal.fire('Éxito', 'La salida se ha registrado correctamente', 'success');
          this.listaDetalleSalida = [];
          this.salidaForm.reset();
          this.router.navigate(['/admin/salidas']);
        },
        error: (err) => {
          console.error(err);
          Swal.fire('Error', 'Hubo un problema al registrar la salida', 'error');
        }
      });
  }
}
