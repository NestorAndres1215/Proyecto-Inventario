import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { firstValueFrom } from 'rxjs';

import { ProductoService } from 'src/app/core/services/producto.service';
import { ReportesService } from 'src/app/core/services/reportes.service';
import { AlertService } from 'src/app/core/services/alert.service';

@Component({
  selector: 'app-listar-producto-activadas',
  templateUrl: './listar-producto-activadas.component.html',
  styleUrls: ['./listar-producto-activadas.component.css'],
})
export class ListarProductoActivadasComponent implements OnInit {
  nombre: string = '';
  productos: any[] = [];

  columnas = [
    { clave: 'productoId', etiqueta: 'Código' },
    { clave: 'nombre', etiqueta: 'Nombre' },
    { clave: 'descripcion', etiqueta: 'Descripción' },
    { clave: 'precio', etiqueta: 'Precio' },
    { clave: 'stock', etiqueta: 'Stock' },
    { clave: 'ubicacion', etiqueta: 'Ubicación' },
    { clave: 'proveedor.nombre', etiqueta: 'Proveedor' },
  ];

  botonesConfig = {
    ver: true,
    editar: true,
    desactivar: true,
  };

  pageSize = 6;
  pageIndex = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private alertService: AlertService,
    private productoService: ProductoService,
    private reporteSalida: ReportesService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.obtenerProducto();
  }

  async obtenerProducto(): Promise<void> {
    try {
      this.productos = await firstValueFrom(
        this.productoService.listarProductosActivos(),
      );
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'No se pudieron cargar los productos.',
      );
    }
  }

  async desactivarProducto(productoId: number): Promise<void> {
    try {
      await firstValueFrom(
        this.productoService.desactivarProducto(productoId),
      );

      this.alertService.aceptacion(
        'Producto desactivado',
        'El producto se desactivó correctamente.',
      );

      await this.obtenerProducto();
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ??
          'Ocurrió un error al desactivar el producto.',
      );
    }
  }

  buscarPorNombre(): void {
    if (this.nombre && this.productos) {
      this.productos = this.productos.filter((producto: any) =>
        producto.nombre
          .toLowerCase()
          .includes(this.nombre.toLowerCase()),
      );
    } else {
      this.restaurarProductos();
    }
  }

  async restaurarProductos(): Promise<void> {
    this.nombre = '';
    await this.obtenerProducto();
  }

  verProducto(producto: any): void {
    this.router.navigate([
      '/admin/producto/detalle',
      producto.productoId,
    ]);
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  async descargarPDF(): Promise<void> {
    try {
      const data = await firstValueFrom(
        this.reporteSalida.descargarProducto(),
      );

      const urlBlob = window.URL.createObjectURL(data);
      const link = document.createElement('a');

      link.href = urlBlob;
      link.download = 'informe_detalle_productos.pdf';
      link.click();

      window.URL.revokeObjectURL(urlBlob);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'No se pudo descargar el reporte.',
      );
    }
  }
}