import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/core/services/login.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-actualizar-usuario',
  templateUrl: './actualizar-usuario.component.html',
  styleUrls: ['./actualizar-usuario.component.css']
})
export class ActualizarUsuarioComponent implements OnInit {
  
  user: any = null;
  id!: number;

  constructor(
    private router: Router,
    private loginService: LoginService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    this.user = this.loginService.getUser();
    
    if (!this.user || !this.user.id) {
      console.error("No se pudo obtener el usuario logueado.");
      this.router.navigate(['/login']);
      return;
    }

    this.id = Number(this.user.id);
  }

  actualizarUsuario(): void {
    if (!this.id || !this.user) {
      console.error('Datos insuficientes para actualizar el usuario');
      return;
    }

    this.usuarioService.actualizarUsuario(this.id, this.user)
      .subscribe({
        next: (respuesta: any) => {
          Swal.fire({
            icon: 'success',
            title: 'Usuario actualizado',
            text: 'Los datos se guardaron correctamente',
            confirmButtonText: 'Aceptar'
          });

          this.router.navigate(['/user-dashboard/configuracion']);
        },
        error: (error: any) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al actualizar usuario',
            text: error?.error || 'Ocurrió un error inesperado',
            confirmButtonText: 'Aceptar'
          });

          console.error('Error al actualizar el usuario:', error);
        }
      });
  }
}
