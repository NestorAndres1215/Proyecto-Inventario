import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-lista-usuario-administrador-desactivados',
  templateUrl: './lista-usuario-administrador-desactivados.component.html',
  styleUrls: ['./lista-usuario-administrador-desactivados.component.css']
})
export class ListaUsuarioAdministradorDesactivadosComponent implements OnInit {

  usuarioRoles: any[] = [];

  constructor(private usuarioRolService: UsuarioService) { }

  ngOnInit(): void {
    this.obtenerUsuarioRoles();
  }

  obtenerUsuarioRoles(): void {
    this.usuarioRolService.obtenerAdminUsuariosDesactivados()
      .subscribe({
        next: (usuarioRoles: any[]) => {
          this.usuarioRoles = usuarioRoles;
        },
        error: (error: any) => {
          console.error('Error al obtener los usuario-roles:', error);
        }
      });
  }

  pageIndex = 0;

  pageSize = 3; // Tamaño de página (número de elementos por página)

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  activarUsuario(usuarioRolId: any): void {
    this.usuarioRolService.activarUsuario(usuarioRolId)
      .subscribe({
        next: (respuesta: any) => {
          Swal.fire({
            icon: 'success',
            title: 'Usuario activado',
            text: respuesta,
            confirmButtonText: 'Aceptar'
          });

          this.obtenerUsuarioRoles(); // Actualiza tabla
        },
        error: (error: any) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al activar usuario',
            text: error?.error || 'Ocurrió un error inesperado.',
            confirmButtonText: 'Aceptar'
          });

          console.error('Error al activar usuario:', error);
        }
      });
  }

}
