import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ALERT_MESSAGES, DetalleEntrada, Producto, Usuario } from 'src/app/core/models/entrada';
import { EntradaService } from 'src/app/core/services/entrada.service';
import { LoginService } from 'src/app/core/services/login.service';
import { ProductoService } from 'src/app/core/services/producto.service';
import Swal from 'sweetalert2';



@Component({
  selector: 'app-registrar-entrada',
  templateUrl: './registrar-entrada.component.html',
  styleUrls: ['./registrar-entrada.component.css'],
})
export class RegistrarEntradaComponent implements OnInit {
  detalleEntradaForm!: FormGroup;
  producto: Producto[] = [];
  listaDetalleEntrada: DetalleEntrada[] = [];
  isLoggedIn = false;
  user: Usuario | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly productoService: ProductoService,
    private readonly loginService: LoginService,
    private readonly entradaService: EntradaService,
    private readonly router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.obtenerProductos();
    this.obtenerUsuario();
  }

  private inicializarFormulario(): void {
    this.detalleEntradaForm = this.fb.group({
      productoId: ['', Validators.required],
      descripcion: ['', Validators.required],
      cantidad: [null, [Validators.required, Validators.min(1)]],
      fechaEntrada: ['', Validators.required],
    });
  }

  private obtenerProductos(): void {
    this.productoService.listarProductosActivos().subscribe({
      next: (data: Producto[]) => (this.producto = data),
      error: (err) => console.error('Error al obtener productos:', err),
    });
  }

  private obtenerUsuario(): void {
    this.actualizarUsuario();
    this.loginService.loginStatusSubject.asObservable().subscribe(() => {
      this.actualizarUsuario();
    });
  }

  private actualizarUsuario(): void {
    this.isLoggedIn = this.loginService.isLoggedIn();
    this.user = this.loginService.getUser();
  }

  agregarProducto(): void {
    if (this.detalleEntradaForm.invalid) {
      Swal.fire('Error', ALERT_MESSAGES.fillFields, 'error');
      return;
    }

    const detalle: DetalleEntrada = {
      producto: { productoId: this.detalleEntradaForm.value.productoId },
      descripcion: this.detalleEntradaForm.value.descripcion,
      cantidad: this.detalleEntradaForm.value.cantidad,
      usuario: { id: this.user!.id },
      entrada: { fechaEntrada: this.detalleEntradaForm.value.fechaEntrada },
    };

    this.listaDetalleEntrada.push(detalle);
    this.detalleEntradaForm.reset();
  }

  enviarEntrada(): void {
    if (this.listaDetalleEntrada.length === 0) {
      Swal.fire('Error', ALERT_MESSAGES.noRecords, 'error');
      return;
    }

    // Asegura que todos los detalles tengan el usuario
    this.listaDetalleEntrada.forEach((d) => (d.usuario.id = this.user!.id));

    this.entradaService.crearEntradaConDetalles(this.listaDetalleEntrada).subscribe({
      next: () => {
        Swal.fire('Éxito', ALERT_MESSAGES.sendSuccess, 'success');
        this.listaDetalleEntrada = [];
        this.detalleEntradaForm.reset();
        this.router.navigate(['/admin/entradas']);
      },
      error: (err) => {
        console.error('Error al enviar entrada:', err);
        Swal.fire('Error', ALERT_MESSAGES.sendError, 'error');
      },
    });
  }

  // Solo permite números positivos
  guardarValor(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9]/g, '');
  }

  eliminarDetalle(index: number): void {
    this.listaDetalleEntrada.splice(index, 1);
  }
}
