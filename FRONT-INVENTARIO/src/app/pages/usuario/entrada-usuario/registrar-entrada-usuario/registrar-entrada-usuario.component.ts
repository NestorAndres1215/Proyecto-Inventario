import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';
import { EntradaService } from 'src/app/core/services/entrada.service';
import { LoginService } from 'src/app/core/services/login.service';
import { ProductoService } from 'src/app/core/services/producto.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-registrar-entrada-usuario',
  templateUrl: './registrar-entrada-usuario.component.html',
  styleUrls: ['./registrar-entrada-usuario.component.css'],
})
export class RegistrarEntradaUsuarioComponent implements OnInit {
  cfechaEntrada: string = '';
  listaDetalleEntrada: any[] = [];
  producto: any[] = [];
  isLoggedIn = false;
  user: any = null;

  detalleEntrada: any = {
    descripcion: '',
    cantidad: '',

    producto: {
      productoId: '',
    },
    usuario: {
      id: '',
    },
    entrada: {
      fechaEntrada: '',
    },
  };

  constructor(
    private readonly productoService: ProductoService,
    private readonly login: LoginService,
    private readonly entradaService: EntradaService,
    private readonly alertService: AlertService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {
    this.obtenerProducto();
    this.obtenerUsuario();
  }

  async enviarEntrada(): Promise<void> {
    if (this.listaDetalleEntrada.length === 0) {
      this.alertService.advertencia(
        'Campos incompletos',
        'Por favor, agrega al menos un producto antes de enviar la entrada.',
      );
      return;
    }

    try {
      this.listaDetalleEntrada.forEach((detalle) => {
        detalle.usuario.id = this.user.id;
      });

      await firstValueFrom(
        this.entradaService.crearEntradaConDetalles(this.listaDetalleEntrada),
      );

      this.listaDetalleEntrada = [];
      this.limpiar();

      this.alertService.aceptacion(
        'Éxito',
        'La entrada se ha enviado correctamente.',
      );

      this.router.navigate(['/user-dashboard/entradas-usuario']);
    } catch (error) {
      console.error('Error al hacer la solicitud:', error);

      this.alertService.error(
        'Error',
        'Hubo un problema al enviar la entrada. Por favor, inténtalo de nuevo.',
      );
    }
  }

  async obtenerProducto(): Promise<void> {
    try {
      this.producto = await firstValueFrom(
        this.productoService.listarProductosActivos(),
      );
    } catch (error) {
      this.alertService.error('Error', 'No se pudieron cargar los productos.');
    }
  }

  agregarProducto() {
    this.listaDetalleEntrada.push({ ...this.detalleEntrada });
    this.limpiar();
  }

  private obtenerUsuario(): void {
    this.cargarUsuario();

    this.login.loginStatusSubject.subscribe(() => {
      this.cargarUsuario();
    });
  }

  private cargarUsuario(): void {
    this.isLoggedIn = this.login.isLoggedIn();
    this.user = this.login.getUser();
  }

  limpiar() {
    this.detalleEntrada = {
      descripcion: '',
      cantidad: null,

      producto: {
        productoId: '',
      },
      usuario: {
        id: '',
      },
      entrada: {
        fechaEntrada: '',
      },
    };
  }

  guardarValor(event: any) {
    const input = event.target as HTMLInputElement;
    let value = input.value;
    value = value.replace(/[^0-9-]/g, '');
    const hasDash = value.startsWith('-');
    value = value.replace(/-/g, '');
    if (hasDash) {
      value = '-' + value;
    }

    input.value = value;
  }
}
