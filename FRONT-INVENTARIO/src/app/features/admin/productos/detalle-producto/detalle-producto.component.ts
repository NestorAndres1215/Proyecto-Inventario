import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { ProductoService } from 'src/app/core/services/producto.service';
import { AlertService } from 'src/app/core/services/alert.service';

@Component({
  selector: 'app-detalle-producto',
  templateUrl: './detalle-producto.component.html',
  styleUrls: ['./detalle-producto.component.css'],
})
export class DetalleProductoComponent implements OnInit {
  producto: any | null = null;
  productoId: number = 0;
  datosProducto: { clave: string; valor: any }[] = [];

  constructor(
    private readonly productoService: ProductoService,
    private readonly route: ActivatedRoute,
    private readonly alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.productoId = Number(
      this.route.snapshot.paramMap.get('productoId'),
    );

    this.cargarProducto();
  }

  private async cargarProducto(): Promise<void> {
    try {
      const data = await firstValueFrom(
        this.productoService.obtenerProductoPorId(this.productoId),
      );

      this.producto = data;

      this.datosProducto = [
        { clave: 'Código', valor: data.productoId },
        { clave: 'Nombre', valor: data.nombre },
        { clave: 'Descripción', valor: data.descripcion },
        { clave: 'Precio', valor: `S/. ${data.precio}` },
        { clave: 'Stock', valor: data.stock },
        { clave: 'Proveedor', valor: data.proveedor?.nombre },
        {
          clave: 'Estado',
          valor: data.estado ? 'Activo' : 'Desactivado',
        },
      ];
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'No se pudo obtener el producto.',
      );
    }
  }
}