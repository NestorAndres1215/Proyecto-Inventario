import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ProductoService } from 'src/app/core/services/producto.service';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import Swal from 'sweetalert2';
import { ReportesService } from 'src/app/core/services/reportes.service';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';

@Component({
  selector: 'app-listar-inventario',
  templateUrl: './listar-inventario.component.html',
  styleUrls: ['./listar-inventario.component.css'],
})
export class ListarInventarioComponent implements OnInit {
  botonesConfig = {
    actualizar: true,
    desactivar: true,
  };

  columnas = [
    { clave: 'productoId', etiqueta: 'Código' },
    { clave: 'nombre', etiqueta: 'Nombre' },
    { clave: 'descripcion', etiqueta: 'Descripción' },
    { clave: 'precio', etiqueta: 'Precio' },
    { clave: 'stock', etiqueta: 'Stock' },
    { clave: 'ubicacion', etiqueta: 'Ubicación' },
    { clave: 'proveedor.nombre', etiqueta: 'Proveedor' },
  ];

  nombre: string = '';
  producto: any = [];
  categoriaId: string = '';
  proveedorId: string = '';
  productos: any[] = [];
  productoId: string = '';

  constructor(
    private productoService: ProductoService,
    private reporteSalida: ReportesService,
    private router: Router,
    private readonly alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.obtenerProducto();
  }

  async obtenerProducto(): Promise<void> {
    try {
      this.productos = await firstValueFrom(
        this.productoService.listarProductosActivos(),
      );
    } catch (error) {
      console.error('Error al obtener los productos:', error);
      this.alertService.error('Error', 'No se pudieron obtener los productos.');
    }
  }

  pageSize = 4;
  pageIndex = 0;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  async desactivarProducto(productoId: number): Promise<void> {
    const confirmado = await this.alertService.confirmacion(
      '¿Desactivar producto?',
      'El producto dejará de estar disponible.',
    );

    if (!confirmado) {
      return;
    }

    try {
      const respuesta = await firstValueFrom(
        this.productoService.desactivarProducto(productoId),
      );

      this.alertService.aceptacion(
        'Producto desactivado',
        respuesta?.mensaje ?? 'El producto fue desactivado correctamente.',
      );

      await this.obtenerProducto();
    } catch (error: any) {
      this.alertService.error(
        'Error al desactivar el producto',
        error?.error?.mensaje ?? 'Ocurrió un error inesperado.',
      );
    }
  }

  async buscarPorNombre(): Promise<void> {
    try {
      if (!this.nombre) {
        await this.restaurarProductos();
        return;
      }

      this.productos = this.productos.filter((producto) =>
        producto.nombre.toLowerCase().includes(this.nombre.toLowerCase()),
      );
    } catch (error) {
      console.error('Error en la búsqueda:', error);
    }
  }

  async restaurarProductos(): Promise<void> {
    this.nombre = '';

    this.productos = await firstValueFrom(
      this.productoService.listarProductosActivos(),
    );
  }

  async descargarPDF(): Promise<void> {
    const data = await firstValueFrom(this.reporteSalida.descargarProducto());

    const blob = new Blob([data], {
      type: 'application/pdf',
    });

    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');

    a.href = url;
    a.download = 'informe_detalle_productos.pdf';
    a.click();

    window.URL.revokeObjectURL(url);
  }

  actualizar(producto: any) {
    this.router.navigate(['/user-dashboard/inventario/', producto.productoId]);
  }
}
