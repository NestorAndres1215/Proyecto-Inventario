import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';

import { AlertService } from 'src/app/core/services/alert.service';
import { ReportesService } from 'src/app/core/services/reportes.service';

import { UsuarioService } from 'src/app/core/services/usuario.service';
import { firstValueFrom } from 'rxjs';
@Component({
  selector: 'app-usuario-operador',
  templateUrl: './usuario-operador.component.html',
  styleUrls: ['./usuario-operador.component.css'],
})
export class UsuarioOperadorComponent implements OnInit {
  usuarioRoles: any[] = [];

  constructor(
    private usuarioRolService: UsuarioService,
    private alertService: AlertService,
    private router: Router,
    private reporteSalida: ReportesService,
  ) {}

  ngOnInit(): void {
    this.obtenerUsuarioRoles();
  }

  botonesConfigTable = {
    ver: true,
    desactivar: true,
  };

  verUsuario(usuarioRol: any) {
    this.router.navigate(['/admin/usuario', usuarioRol.id]);
  }

async obtenerUsuarioRoles(): Promise<void> {
  this.usuarioRoles = await firstValueFrom(
    this.usuarioRolService.obtenerUsuariosNormalesActivos(),
  );
}
  columnas = [
    { etiqueta: 'Nombre', clave: 'nombre' },
    { etiqueta: 'Apellido', clave: 'apellido' },
    { etiqueta: 'Correo', clave: 'email' },
    { etiqueta: 'Telefono', clave: 'telefono' },
  ];

  pageSize = 3;
  pageIndex = 0;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  async desactivarUsuario(usuarioRolId: any): Promise<void> {
    try {
      await firstValueFrom(
        this.usuarioRolService.desactivarUsuario(usuarioRolId),
      );

      this.alertService.advertencia(
        'Usuario desactivado',
        'El usuario fue desactivado correctamente',
      );

      this.obtenerUsuarioRoles();
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al desactivar el usuario.',
      );
    }
  }

  async descargarPDF(): Promise<void> {
    try {
      const data = await firstValueFrom(
        this.reporteSalida.descargarUsuarioOperador(),
      );

      const blob = new Blob([data], {
        type: 'application/pdf',
      });

      const urlBlob = window.URL.createObjectURL(blob);
      const a = document.createElement('a');

      a.href = urlBlob;
      a.download = 'informe_detalle_salidas_productos.pdf';
      a.click();

      window.URL.revokeObjectURL(urlBlob);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'No se pudo descargar el PDF.',
      );
    }
  }
}
