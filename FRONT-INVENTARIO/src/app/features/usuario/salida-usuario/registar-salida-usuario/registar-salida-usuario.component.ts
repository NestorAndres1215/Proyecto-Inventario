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
  selector: 'app-registar-salida-usuario',
  templateUrl: './registar-salida-usuario.component.html',
  styleUrls: ['./registar-salida-usuario.component.css'],
})
export class RegistarSalidaUsuarioComponent implements OnInit {
  salidaForm!: FormGroup;
  productos: any[] = [];
  listaDetalleSalida: Salida[] = [];
  user: any = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly alertService: AlertService,
    private readonly productoService: ProductoService,
    private readonly loginService: LoginService,
    private readonly salidaService: SalidaService,
    private readonly router: Router,
  ) {}

  async ngOnInit(): Promise<void> {
    this.salidaForm = this.fb.group({
      producto: ['', Validators.required],
      descripcion: ['', Validators.required],
      cantidad: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      fechaSalida: ['', Validators.required],
    });

    this.user = this.loginService.getUser();
    await this.obtenerProducto();
  }

  async obtenerProducto(): Promise<void> {
    try {
      this.productos = await firstValueFrom(
        this.productoService.listarProductosActivos(),
      );
    } catch (error) {
      console.error('Error al obtener productos:', error);

      this.alertService.error('Error', 'No se pudieron cargar los productos.');
    }
  }
  isLoggedIn: any;

  agregarProducto(): void {
    if (this.salidaForm.invalid) {
      this.alertService.advertencia(
        'Campos incompletos',
        'Complete todos los campos.',
      );
      return;
    }

    const { producto, descripcion, cantidad, fechaSalida } =
      this.salidaForm.getRawValue();

    if (this.listaDetalleSalida.some((d) => d.producto === producto.nombre)) {
      this.alertService.advertencia(
        'Producto duplicado',
        'El producto ya fue agregado.',
      );
      return;
    }

    const detalle: Salida = {
      cantidad,
      descripcion,
      usuario: this.user.username,
      producto: producto.nombre,
      fechaSalida,
    };

    this.listaDetalleSalida.push(detalle);

    this.alertService.aceptacion(
      'Producto agregado',
      'El producto se agregó correctamente.',
    );

    this.salidaForm.reset({ fechaSalida });
  }

  async enviarSalida(): Promise<void> {
    if (this.listaDetalleSalida.length === 0) {
      this.alertService.advertencia(
        'Sin registros',
        'Agregue al menos un producto antes de enviar.',
      );
      return;
    }

    try {
      await firstValueFrom(
        this.salidaService.crearSalidaConDetalles(this.listaDetalleSalida),
      );

      this.alertService.aceptacion(
        'Éxito',
        'La salida se registró correctamente.',
      );

      this.listaDetalleSalida = [];
      this.salidaForm.reset();

      this.router.navigate(['/user-dashboard/salidas']);
    } catch (error) {
      console.error('Error al registrar la salida:', error);

      this.alertService.error(
        'Error',
        'Hubo un problema al registrar la salida.',
      );
    }
  }
}
