import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import {
  ALERT_MESSAGES,
  DetalleEntrada,
  Producto,
  Usuario,
} from 'src/app/core/models/entrada';
import { AlertService } from 'src/app/core/services/alert.service';
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
    private readonly router: Router,
    private readonly alertService: AlertService,
  ) {}

  async ngOnInit(): Promise<void> {
    this.inicializarFormulario();
    await this.obtenerProductos();
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

  private async obtenerProductos(): Promise<void> {
    try {
      this.producto = await firstValueFrom(
        this.productoService.listarProductosActivos(),
      );
    } catch (error) {
      this.alertService.error('Error', 'No se pudieron cargar los productos.');
    }
  }

  private obtenerUsuario(): void {
    this.actualizarUsuario();

    this.loginService.loginStatusSubject.subscribe(() => {
      this.actualizarUsuario();
    });
  }

  private actualizarUsuario(): void {
    this.isLoggedIn = this.loginService.isLoggedIn();
    this.user = this.loginService.getUser();
  }

  agregarProducto(): void {
    if (this.detalleEntradaForm.invalid) {
      this.alertService.advertencia(
        'Campos incompletos',
        'Complete todos los campos antes de agregar el producto.',
      );
      this.detalleEntradaForm.markAllAsTouched();
      return;
    }

    console.log(this.user)
const detalle: DetalleEntrada = {
  producto: { productoId: this.detalleEntradaForm.value.productoId },
  descripcion: this.detalleEntradaForm.value.descripcion,
  cantidad: this.detalleEntradaForm.value.cantidad,
  usuario: { id: this.user!.id },
  entrada: {
    fechaEntrada: this.detalleEntradaForm.value.fechaEntrada,
    usuario: { id: this.user!.id }
  }
};

    this.listaDetalleEntrada.push(detalle);
    this.detalleEntradaForm.reset();
  }

  async enviarEntrada(): Promise<void> {
    if (this.listaDetalleEntrada.length === 0) {
      this.alertService.advertencia(
        'Sin registros',
        'Agregue al menos un producto antes de enviar la entrada.',
      );
      return;
    }

    try {
      if (!this.user) {
        this.alertService.error(
          'Error',
          'No se encontró el usuario autenticado.',
        );
        return;
      }

      const usuario = this.user;

      this.listaDetalleEntrada.forEach((detalle) => {
        detalle.usuario.id = usuario.id;
      });

      await firstValueFrom(
        this.entradaService.crearEntradaConDetalles(this.listaDetalleEntrada),
      );

      this.alertService.aceptacion(
        'Éxito',
        'La entrada se registró correctamente.',
      );

      this.listaDetalleEntrada = [];
      this.detalleEntradaForm.reset();

      this.router.navigate(['/admin/entradas']);
    } catch (error) {
      console.error('Error al enviar entrada:', error);

      this.alertService.error('Error', 'No se pudo registrar la entrada.');
    }
  }



  guardarValor(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9]/g, '');
  }

  eliminarDetalle(index: number): void {
    this.listaDetalleEntrada.splice(index, 1);
  }
}
