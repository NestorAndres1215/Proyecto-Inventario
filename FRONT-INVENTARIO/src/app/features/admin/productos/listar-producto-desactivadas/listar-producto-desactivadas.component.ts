import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AlertService } from 'src/app/core/services/alert.service';
import { ProductoService } from 'src/app/core/services/producto.service';

@Component({
  selector: 'app-listar-producto-desactivadas',
  templateUrl: './listar-producto-desactivadas.component.html',
  styleUrls: ['./listar-producto-desactivadas.component.css'],
})
export class ListarProductoDesactivadasComponent implements OnInit {
  nombre: string = '';
  producto: any = [];
  categoriaId: string = '';
  proveedorId: string = '';
  productos: any[] = [];

  constructor(
    private alertService: AlertService,
    private productoService: ProductoService,
    private router: Router,
  ) {}

  botonesConfig = {
    ver: true,
    activar: true,
  };
  ngOnInit(): void {
    this.obtenerProducto();
  }

  verProducto(producto: any) {
    this.router.navigate(['/admin/producto/detalle', producto.productoId]);
  }

  buscarPorNombre() {
    if (this.nombre && this.productos) {
      this.productos = this.productos.filter((proveedor: any) =>
        proveedor.nombre.toLowerCase().includes(this.nombre.toLowerCase()),
      );
    } else {
      this.restaurarProveedores();
    }
  }
  columnas = [
    { clave: 'productoId', etiqueta: 'Código' },
    { clave: 'nombre', etiqueta: 'Nombre' },
    { clave: 'descripcion', etiqueta: 'Descripción' },
    { clave: 'precio', etiqueta: 'Precio' },
    { clave: 'stock', etiqueta: 'Stock' },
    { clave: 'proveedor.nombre', etiqueta: 'Proveedor' },
  ];

  async restaurarProveedores(): Promise<void> {
    this.nombre = '';

    this.productos = await firstValueFrom(
      this.productoService.listarProductosDesactivados(),
    );
  }

  async obtenerProducto(): Promise<void> {
    try {
      const productos = await firstValueFrom(
        this.productoService.listarProductosDesactivados(),
      );

      this.productos = productos;
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'No se pudieron obtener los productos.',
      );
    }
  }

  pageSize = 3;
  pageIndex = 0;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  async activarProducto(productoId: number): Promise<void> {
    try {
      await firstValueFrom(this.productoService.activarProducto(productoId));

      this.alertService.aceptacion(
        'Producto activado',
        'El producto se activó correctamente',
      );

      this.obtenerProducto();
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al activar el producto.',
      );
    }
  }
}
