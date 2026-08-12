import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AlertService } from 'src/app/core/services/alert.service';
import { ReportesService } from 'src/app/core/services/reportes.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';

@Component({
  selector: 'app-usuario-administrador',
  templateUrl: './usuario-administrador.component.html',
  styleUrls: ['./usuario-administrador.component.css'],
})
export class UsuarioAdministradorComponent implements OnInit {
  usuarioRoles: any = [];
  nombre: string = '';
  usuarioAutenticadoId: number = 1;

  constructor(
    private router: Router,
    private usuarioRolService: UsuarioService,
    private alertService: AlertService,
    private reporteSalida: ReportesService,
  ) {}

  botonesConfigTable = {
    ver: true,
    desactivar: true,
  };

  columnas = [
    { etiqueta: 'Nombre', clave: 'nombre' },
    { etiqueta: 'Apellido', clave: 'apellido' },
    { etiqueta: 'Correo', clave: 'email' },
    { etiqueta: 'Telefono', clave: 'telefono' },
  ];

  ngOnInit(): void {
    this.obtenerUsuarioRoles();
  }

  verUsuario(usuarioRol: any) {
    this.router.navigate(['/admin/usuario', usuarioRol.id]);
  }

  async obtenerUsuarioRoles(): Promise<void> {
    try {
      this.usuarioRoles = await firstValueFrom(
        this.usuarioRolService.obtenerAdminUsuariosActivos(),
      );
    } catch (error) {
      console.error('Error al obtener los usuarios:', error);

      this.alertService.error('Error', 'No se pudieron cargar los usuarios.');
    }
  }

  pageSize = 3;
  pageIndex = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  async desactivarUsuario(usuarioRolId: number): Promise<void> {
    try {
      await firstValueFrom(
        this.usuarioRolService.desactivarUsuario(usuarioRolId),
      );

      this.alertService.aceptacion(
        'Usuario desactivado',
        'El usuario fue desactivado correctamente.',
      );

      this.obtenerUsuarioRoles();
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error?.error?.message ?? 'Ocurrió un error al desactivar el usuario.',
      );
    }
  }

  async descargarPDF(): Promise<void> {
    try {
      const data = await firstValueFrom(this.reporteSalida.descargarSalida());

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
        error?.error?.message ?? 'No se pudo descargar el PDF.',
      );
    }
  }
}
