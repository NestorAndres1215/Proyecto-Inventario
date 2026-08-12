import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';
import { LoginService } from 'src/app/core/services/login.service';
import { UsuarioService } from 'src/app/core/services/usuario.service';

@Component({
  selector: 'app-actualizar-usuario-usuario',
  templateUrl: './actualizar-usuario-usuario.component.html',
  styleUrls: ['./actualizar-usuario-usuario.component.css'],
})
export class ActualizarUsuarioUsuarioComponent implements OnInit {
  user: any = null;
  id = 0;

  constructor(
    private readonly router: Router,
    private readonly loginService: LoginService,
    private readonly usuarioService: UsuarioService,
    private readonly alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.user = this.loginService.getUser();
    this.id = this.user?.id ?? 0;
  }

  async actualizarUsuario(): Promise<void> {
    if (!this.id || !this.user) {
      this.alertService.error(
        'Error',
        'No se encontraron los datos del usuario.',
      );
      return;
    }

    try {
      await firstValueFrom(
        this.usuarioService.actualizarUsuario(this.id, this.user),
      );

      this.alertService.aceptacion(
        'Usuario actualizado',
        'Los datos del usuario se actualizaron correctamente.',
      );

      this.router.navigate(['/user-dashboard/configuracion']);
    } catch (error) {
      this.alertService.error('Error', 'No se pudo actualizar el usuario.');
    }
  }
}
