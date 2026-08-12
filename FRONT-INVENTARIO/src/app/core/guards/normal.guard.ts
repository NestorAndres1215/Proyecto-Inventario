import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { ROLES } from '../constants/rol';
import { LoginService } from 'src/app/core/services/login.service';

@Injectable({
  providedIn: 'root'
})
export class NormalGuard implements CanActivate {

  constructor(
    private readonly loginService: LoginService,
    private readonly router: Router
  ) {}

  canActivate(): boolean | UrlTree {
    if (!this.loginService.isLoggedIn()) {
      return this.router.createUrlTree(['/login']);
    }

    if (this.loginService.getUserRole() !== ROLES.NORMAL) {
      return this.router.createUrlTree(['/admin']);
    }

    return true;
  }
}