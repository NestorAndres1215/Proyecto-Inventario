import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { Salida } from 'src/app/core/models/detalle-salidad';
import { AlertService } from 'src/app/core/services/alert.service';

import { LoginService } from 'src/app/core/services/login.service';
import { ProductoService } from 'src/app/core/services/producto.service';
import { SalidaService } from 'src/app/core/services/salida.service';

@Component({
  selector: 'app-registrar-salidas',
  templateUrl: './registrar-salidas.component.html',
  styleUrls: ['./registrar-salidas.component.css'],
})
export class RegistrarSalidasComponent implements OnInit {
  salidaForm!: FormGroup;
  productos: any[] = [];
  listaDetalleSalida: any[] = [];
  user: any = null;
  producto: any;
  isLoggedIn: any;

  constructor(
    private fb: FormBuilder,
    private alertSerrvice: AlertService,
    private productoService: ProductoService,
    private loginService: LoginService,
    private salidaService: SalidaService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.salidaForm = this.fb.group({
      producto: ['', Validators.required],
      descripcion: ['', Validators.required],
      cantidad: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      fechaSalida: ['', Validators.required],
    });

    this.obtenerProducto();
    this.obtenerUsuario();
  }

  async obtenerProducto(): Promise<void> {
    this.productos = await firstValueFrom(
      this.productoService.listarProductosActivos(),
    );
  }

  obtenerUsuario(): void {
    this.user = this.loginService.getUser();
  }

  agregarProducto(): void {
    if (this.salidaForm.invalid) {
      this.alertSerrvice.advertencia('Advertencia', 'Hay campos incompletos');
      return;
    }

    const { producto, descripcion, cantidad, fechaSalida } =
      this.salidaForm.value;

    if (this.listaDetalleSalida.some((d) => d.producto.nombre === producto)) {
      this.alertSerrvice.advertencia(
        'Advertencia',
        'El producto ya ha sido registrado',
      );
      return;
    }

    const detalle: Salida = {
      cantidad: cantidad,
      descripcion: descripcion,
      usuario: this.loginService.getUser().username,
      producto: producto.nombre,
      fechaSalida: fechaSalida,
    };

    this.listaDetalleSalida.push(detalle);
    this.alertSerrvice.aceptacion(
      'Registro exitoso',
      'El registro se realizó correctamente',
    );
    this.salidaForm.reset({ fechaSalida });
  }

  async enviarSalida(): Promise<void> {
    if (this.listaDetalleSalida.length === 0) {
      this.alertSerrvice.advertencia(
        'Sin registros',
        'Agregue al menos un producto antes de enviar.',
      );
      return;
    }

    try {
      await firstValueFrom(
        this.salidaService.crearSalidaConDetalles(this.listaDetalleSalida),
      );

      this.alertSerrvice.aceptacion(
        'Éxito',
        'La salida se ha registrado correctamente',
      );

      this.listaDetalleSalida = [];
      this.salidaForm.reset();
      this.router.navigate(['/admin/salidas']);
    } catch (error) {
      this.alertSerrvice.error(
        'Error',
        'Hubo un problema al registrar la salida',
      );
    }
  }
}
