import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';

@Component({
  selector: 'app-lista-usuario-administrador-desactivados',
  templateUrl: './lista-usuario-administrador-desactivados.component.html',
  styleUrls: ['./lista-usuario-administrador-desactivados.component.css'],
})
export class ListaUsuarioAdministradorDesactivadosComponent implements OnInit {
  botonesConfigTable = {
    ver: true,
    desactivar: true,
  };

  usuarioRoles: any[] = [];

  columnas = [
    { etiqueta: 'Nombre', clave: 'nombre' },
    { etiqueta: 'Apellido', clave: 'apellido' },
    { etiqueta: 'Correo', clave: 'email' },
    { etiqueta: 'Telefono', clave: 'telefono' },
  ];
  constructor(
    private usuarioRolService: UsuarioService,
    private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.obtenerUsuarioRoles();
  }

  async obtenerUsuarioRoles(): Promise<void> {
    this.usuarioRoles = await firstValueFrom(
      this.usuarioRolService.obtenerAdminUsuariosDesactivados(),
    );
  }

  pageIndex = 0;
  pageSize = 3;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  async activarUsuario(usuarioRolId: any): Promise<void> {
    try {
      await firstValueFrom(this.usuarioRolService.activarUsuario(usuarioRolId));

      this.alertService.advertencia(
        'Usuario activado',
        'El usuario fue activado correctamente.',
      );

      this.obtenerUsuarioRoles();
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al activar el usuario.',
      );
    }
  }
}
