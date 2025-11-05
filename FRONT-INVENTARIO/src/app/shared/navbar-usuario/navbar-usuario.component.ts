import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/core/services/login.service';

@Component({
  selector: 'app-navbar-usuario',
  templateUrl: './navbar-usuario.component.html',
  styleUrls: ['./navbar-usuario.component.css']
})
export class NavbarUsuarioComponent implements OnInit {
  
  isActive(path: string): boolean {
    return this.router.url === path;
  }

  isLoggedIn = false;
  user: any = null;
  contenido: any;

  constructor(public login: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.isLoggedIn = this.login.isLoggedIn();
    this.user = this.login.getUser();
    this.login.loginStatusSubject.asObservable().subscribe(
      data => {
        this.isLoggedIn = this.login.isLoggedIn();
        this.user = this.login.getUser();
      }
    )
  }

  logout() {
    this.login.logout();
    window.location.href = '';
  }

  hayContenidoEnPagina(): boolean {

    return !!this.contenido;
  }

  status = false;
  addToggle() {
    this.status = !this.status;
  }

}
