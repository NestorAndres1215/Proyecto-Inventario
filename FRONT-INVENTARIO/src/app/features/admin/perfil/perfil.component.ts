import { Component, OnInit } from '@angular/core';
import { LoginService } from 'src/app/core/services/login.service';

interface Usuario {
  id: number;
  username: string;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  authorities?: { authority: string }[];
}

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css'],
})
export class PerfilComponent implements OnInit {

  user: Usuario | null = null;

  datosUsuario: { clave: string; valor: string }[] = [];

  constructor(
    private readonly loginService: LoginService
  ) {}

  ngOnInit(): void {

    this.user = this.loginService.getUser() as Usuario | null;

    if (!this.user) {
      return;
    }

    this.datosUsuario = [
      {
        clave: 'Correo',
        valor: this.user.email
      },
      {
        clave: 'Nombre de usuario',
        valor: this.user.username
      },
      {
        clave: 'Rol',
        valor: this.user.authorities?.[0]?.authority ?? ''
      },
      {
        clave: 'Teléfono',
        valor: this.user.telefono ?? ''
      }
    ].sort((a, b) =>
      a.clave.localeCompare(b.clave)
    );
  }
}