import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AlertService } from 'src/app/core/services/alert.service';
import { LoginService } from 'src/app/core/services/login.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-actualizar-usuario',
  templateUrl: './actualizar-usuario.component.html',
  styleUrls: ['./actualizar-usuario.component.css'],
})
export class ActualizarUsuarioComponent implements OnInit {
  user: any = null;
  id!: number;

  constructor(
    private router: Router,
    private alertService: AlertService,
    private loginService: LoginService,
    private usuarioService: UsuarioService,
  ) {}

  ngOnInit(): void {
    this.user = this.loginService.getUser();

    if (!this.user || !this.user.id) {
      this.router.navigate(['/login']);
      return;
    }

    this.id = Number(this.user.id);
  }

  async actualizarUsuario(): Promise<void> {
    if (!this.id || !this.user) {
      return;
    }

    try {
      await firstValueFrom(
        this.usuarioService.actualizarUsuario(this.id, this.user),
      );

      this.alertService.aceptacion(
        'Actualización exitosa',
        'El usuario se actualizó correctamente.',
      );

      await this.router.navigate(['/user-dashboard/configuracion']);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'Ocurrió un error al actualizar el usuario.',
      );
    }
  }
}
