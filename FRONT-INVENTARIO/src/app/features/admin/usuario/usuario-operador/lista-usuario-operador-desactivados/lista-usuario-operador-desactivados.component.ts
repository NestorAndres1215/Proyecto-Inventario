import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';


@Component({
  selector: 'app-lista-usuario-operador-desactivados',
  templateUrl: './lista-usuario-operador-desactivados.component.html',
  styleUrls: ['./lista-usuario-operador-desactivados.component.css'],
})
export class ListaUsuarioOperadorDesactivadosComponent implements OnInit {
  usuarioRoles: any[] = [];

  constructor(
    private usuarioRolService: UsuarioService,
    private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.obtenerUsuarioRoles();
  }
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

  async obtenerUsuarioRoles(): Promise<void> {
    try {
      this.usuarioRoles = await firstValueFrom(
        this.usuarioRolService.obtenerUsuariosNormalesDesactivados(),
      );
    } catch (error) {
      console.error('Error al obtener los usuario-roles:', error);
    }
  }

  pageSize = 3; // Tamaño de página (número de elementos por página)
  pageIndex = 0; //
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  async activarUsuario(usuarioRolId: any): Promise<void> {
    try {
      await firstValueFrom(this.usuarioRolService.activarUsuario(usuarioRolId));

      this.alertService.aceptacion(
        'Usuario activado',
        'El usuario se activó correctamente.',
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
