import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { ProveedorService } from 'src/app/core/services/proveedor.service';
import { AlertService } from 'src/app/core/services/alert.service';

@Component({
  selector: 'app-detalle-proveedor',
  templateUrl: './detalle-proveedor.component.html',
  styleUrls: ['./detalle-proveedor.component.css'],
})
export class DetalleProveedorComponent implements OnInit {
  proveedor: any;
  proveedorId: number = 0;
  datosUsuario: { clave: string; valor: any }[] = [];

  constructor(
    private proveedorService: ProveedorService,
    private alertService: AlertService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.proveedorId = this.route.snapshot.params['proveedorId'];
    this.obtenerProveedorPorId(this.proveedorId);
  }

  async obtenerProveedorPorId(proveedorId: number): Promise<void> {
    try {
      const data = await firstValueFrom(
        this.proveedorService.obtenerProveedorPorId(proveedorId),
      );

      this.proveedor = data;

      this.datosUsuario = [
        { clave: 'Código', valor: data.proveedorId },
        { clave: 'Nombre', valor: data.nombre },
        { clave: 'Teléfono', valor: data.telefono },
        { clave: 'Correo', valor: data.email },
        { clave: 'RUC', valor: data.ruc },
        { clave: 'Dirección', valor: data.direccion },
        {
          clave: 'Estado',
          valor: data.estado ? 'Activo' : 'Desactivado',
        },
      ];
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'No se pudo obtener el proveedor.',
      );
    }
  }
}