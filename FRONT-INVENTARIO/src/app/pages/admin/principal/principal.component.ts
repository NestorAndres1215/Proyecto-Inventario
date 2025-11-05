import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../../core/services/login.service';

interface Usuario {
  id: number;
  nombre?: string;
  email?: string;
  // Agrega otros campos según tu modelo de usuario
}

@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.css'],
})
export class PrincipalComponent implements OnInit {
  isLoggedIn = false;
  user: any | null = null;

  constructor(private readonly loginService: LoginService) {}

  ngOnInit(): void {
    this.actualizarUsuario();
    // Escucha cambios en el estado de login
    this.loginService.loginStatusSubject.asObservable().subscribe(() => {
      this.actualizarUsuario();
    });
  }

  private actualizarUsuario(): void {
    this.isLoggedIn = this.loginService.isLoggedIn();
    this.user = this.loginService.getUser();
  }
}
