import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/core/services/login.service';

@Component({
  selector: 'app-navbar-admin',
  templateUrl: './navbar-admin.component.html',
  styleUrls: ['./navbar-admin.component.css']
})
export class NavbarAdminComponent implements OnInit {

  isLoggedIn = false;
  user: any = null;
  contenido: any;
  status = false;

  constructor(
    private readonly loginService: LoginService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuario();
  }

  isActive(path: string): boolean {
    return this.router.url === path;
  }

  private cargarUsuario(): void {
    this.isLoggedIn = this.loginService.isLoggedIn();
    this.user = this.loginService.getUser();
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  hayContenidoEnPagina(): boolean {
    return !!this.contenido;
  }

  addToggle(): void {
    this.status = !this.status;
  }
}