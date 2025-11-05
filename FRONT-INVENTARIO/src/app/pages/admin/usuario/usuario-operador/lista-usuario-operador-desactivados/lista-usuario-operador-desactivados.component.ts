import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-lista-usuario-operador-desactivados',
  templateUrl: './lista-usuario-operador-desactivados.component.html',
  styleUrls: ['./lista-usuario-operador-desactivados.component.css']
})
export class ListaUsuarioOperadorDesactivadosComponent implements OnInit {

  usuarioRoles: any[] = [];

  constructor(private usuarioRolService: UsuarioService) { }

  ngOnInit(): void {
    this.obtenerUsuarioRoles();
  }

  obtenerUsuarioRoles(): void {
    this.usuarioRolService.obtenerUsuariosNormalesDesactivados()
      .subscribe({
        next: (usuarioRoles: any[]) => {
          this.usuarioRoles = usuarioRoles;
        },
        error: (error: any) => {
          console.error('Error al obtener los usuario-roles:', error);
        }
      });
  }

  pageSize = 3; // Tamaño de página (número de elementos por página)
  pageIndex = 0; // 
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

          this.obtenerUsuarioRoles(); // Refrescar tabla
        },
        error: (error: any) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al activar usuario',
            text: error?.error || 'Ocurrió un error inesperado',
            confirmButtonText: 'Aceptar'
          });

          console.error('Error al activar usuario:', error);
        }
      });
  }


}
