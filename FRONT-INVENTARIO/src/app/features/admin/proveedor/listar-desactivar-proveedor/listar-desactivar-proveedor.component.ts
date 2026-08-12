import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AlertService } from 'src/app/core/services/alert.service';
import { ProveedorService } from 'src/app/core/services/proveedor.service';

@Component({
  selector: 'app-listar-desactivar-proveedor',
  templateUrl: './listar-desactivar-proveedor.component.html',
  styleUrls: ['./listar-desactivar-proveedor.component.css'],
})
export class ListarDesactivarProveedorComponent implements OnInit {
  nombre: string = '';

  proveedores: any[] = [];
  proveedoresOriginal: any[] = [];

  columnas = [
    { clave: 'nombre', etiqueta: 'Nombre' },
    { clave: 'telefono', etiqueta: 'Teléfono' },
    { clave: 'ruc', etiqueta: 'RUC' },
    { clave: 'email', etiqueta: 'Correo' },
    { clave: 'direccion', etiqueta: 'Dirección' },
  ];

  botonesConfig = {
    ver: true,
    activar: true,
  };

  constructor(
    private alertService: AlertService,
    private proveedorService: ProveedorService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.obtenerProveedoresDesactivados();
  }

  async obtenerProveedoresDesactivados(): Promise<void> {
    try {
      const data = await firstValueFrom(
        this.proveedorService.listarProveedoresDesactivados(),
      );

      this.proveedoresOriginal = data;
      this.proveedores = data;
    } catch (error) {
      console.error('Error al obtener proveedores desactivados:', error);
    }
  }

  buscarPorNombre(): void {
    const valor = this.nombre.trim().toLowerCase();

    if (!valor) {
      this.proveedores = this.proveedoresOriginal;
      return;
    }

    this.proveedores = this.proveedoresOriginal.filter(
      (p) =>
        p.nombre?.toLowerCase().includes(valor) ||
        p.ruc?.toLowerCase().includes(valor),
    );
  }

  verProveedor(item: any) {
    return ['/admin/proveedor/detalle', item.proveedorId];
  }

  async activarProveedor(item: any): Promise<void> {
    try {
      await firstValueFrom(
        this.proveedorService.activarProveedor(item.proveedorId),
      );

      this.alertService.aceptacion(
        'Proveedor activado',
        'El proveedor fue activado correctamente.',
      );

      await this.obtenerProveedoresDesactivados();
      await this.router.navigate(['/admin/proveedor']);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al activar el proveedor.',
      );
    }
  }
}
