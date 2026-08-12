import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AlertService } from 'src/app/core/services/alert.service';
import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { ReportesService } from 'src/app/core/services/reportes.service';

@Component({
  selector: 'app-listar-activadas-proveedor',
  templateUrl: './listar-activadas-proveedor.component.html',
  styleUrls: ['./listar-activadas-proveedor.component.css'],
})
export class ListarActivadasProveedorComponent implements OnInit {
  nombre: string = '';
  ruc: string = '';
  proveedores: any[] = [];

  columnas = [
    { clave: 'nombre', etiqueta: 'Nombre' },
    { clave: 'telefono', etiqueta: 'Teléfono' },
    { clave: 'ruc', etiqueta: 'RUC' },
    { clave: 'email', etiqueta: 'Correo' },
    { clave: 'direccion', etiqueta: 'Dirección' },
  ];

  botonesConfig = {
    ver: true,
    editar: true,
    desactivar: true,
  };

  proveedorId: string = '';
  productos: any;

  pageSize = 5;
  pageIndex = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private alertService: AlertService,
    private router: Router,
    private proveedorService: ProveedorService,
    private reporteSalida: ReportesService,
  ) {}

  ngOnInit(): void {
    this.obtenerProveedr();
  }

  verProveedor(item: any): void {
    this.router.navigate(['/admin/proveedor/detalle', item.proveedorId]);
  }

  editarProveedor(item: any) {
    return ['/admin/proveedor', item.proveedorId];
  }

  async obtenerProveedr(): Promise<void> {
    try {
      this.proveedores = await firstValueFrom(
        this.proveedorService.listarProveedoresActivos(),
      );
    } catch (error) {
      console.error('Error al obtener proveedores:', error);
    }
  }

  async restaurarProveedores(): Promise<void> {
    this.nombre = '';

    try {
      this.proveedores = await firstValueFrom(
        this.proveedorService.listarProveedoresActivos(),
      );
    } catch (error) {
      console.error('Error al restaurar proveedores:', error);
    }
  }

  buscarPorNombre(): void {
    if (this.nombre && this.proveedores) {
      this.proveedores = this.proveedores.filter(
        (proveedor: any) =>
          proveedor.nombre
            .toLowerCase()
            .includes(this.nombre.toLowerCase()) ||
          proveedor.ruc
            .toLowerCase()
            .includes(this.nombre.toLowerCase()),
      );
    } else {
      this.restaurarProveedores();
    }
  }

  async desactivarProveedor(proveedorId: any): Promise<void> {
    try {
      await firstValueFrom(
        this.proveedorService.desactivarProveedor(proveedorId),
      );

      this.alertService.advertencia(
        'Proveedor desactivado',
        'El proveedor fue desactivado correctamente.',
      );

      await this.obtenerProveedr();
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al desactivar el proveedor.',
      );
    }
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  async descargarPDF(): Promise<void> {
    try {
      const data = await firstValueFrom(
        this.reporteSalida.descargarProveedor(),
      );

      const urlBlob = window.URL.createObjectURL(data);
      const link = document.createElement('a');

      link.href = urlBlob;
      link.download = 'informe_detalle_proveedor.pdf';
      link.click();

      window.URL.revokeObjectURL(urlBlob);
    } catch (error) {
      this.alertService.error(
        'Error',
        'No se pudo descargar el reporte.',
      );
    }
  }
}