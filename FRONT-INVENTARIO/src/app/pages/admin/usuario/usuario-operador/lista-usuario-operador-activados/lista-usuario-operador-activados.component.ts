import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { ReportesService } from 'src/app/core/services/reportes.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-lista-usuario-operador-activados',
  templateUrl: './lista-usuario-operador-activados.component.html',
  styleUrls: ['./lista-usuario-operador-activados.component.css']
})
export class ListaUsuarioOperadorActivadosComponent implements OnInit {

  usuarioRoles: any[] = [];

  constructor(private usuarioRolService: UsuarioService,
    private reporteSalida: ReportesService) { }

  ngOnInit(): void {
    this.obtenerUsuarioRoles();
  }

  obtenerUsuarioRoles(): void {
    this.usuarioRolService.obtenerUsuariosNormalesActivos()
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
  desactivarUsuario(usuarioRolId: any): void {
    this.usuarioRolService.desactivarUsuario(usuarioRolId)
      .subscribe({
        next: (respuesta: any) => {
          Swal.fire({
            icon: 'success',
            title: 'Usuario desactivado',
            text: respuesta,
            confirmButtonText: 'Aceptar'
          });

          this.obtenerUsuarioRoles(); // Refrescar la tabla
        },
        error: (error: any) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al desactivar usuario',
            text: error?.error || 'Ocurrió un error inesperado',
            confirmButtonText: 'Aceptar'
          });

          console.error('Error al desactivar usuario:', error);
        }
      });
  }

  descargarPDF() {
    this.reporteSalida.descargarUsuarioOperador().subscribe((data: Blob) => {
      const blob = new Blob([data], { type: 'application/pdf' });
      const urlBlob = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = urlBlob;
      a.download = 'informe_detalle_salidas_productos.pdf';
      a.style.display = 'none';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(urlBlob);
      document.body.removeChild(a);
    });
  }

}
