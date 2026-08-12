import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { LoginService } from 'src/app/core/services/login.service';
import { ROLES } from '../constants/rol';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(
    private readonly loginService: LoginService,
    private readonly router: Router,
  ) {}

  canActivate(): boolean | UrlTree {
    if (!this.loginService.isLoggedIn()) {
      return this.router.createUrlTree(['/login']);
    }

    const user = this.loginService.getUser();

    if (user?.authorities?.[0]?.authority !== ROLES.ADMIN) {
      return this.router.createUrlTree(['/user-dashboard']);
    }

    return true;
  }
}
