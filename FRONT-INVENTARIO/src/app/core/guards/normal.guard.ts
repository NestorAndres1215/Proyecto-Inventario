import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from 'src/app/core/services/login.service';
import { ROUTES } from 'src/app/core/constants/routes.constants';
import { ROLES } from 'src/app/core/constants/roles.constants';
import { MESSAGES } from 'src/app/core/constants/messages.constants';

@Injectable({
  providedIn: 'root'
})
export class NormalGuard implements CanActivate {

  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const isLogged = this.loginService.isLoggedIn();
    const userRole = this.loginService.getUserRole();

    if (isLogged && userRole === ROLES.NORMAL) {
      console.info(MESSAGES.ACCESS_GRANTED_NORMAL);
      return true;
    }

    console.warn(MESSAGES.ACCESS_DENIED_NORMAL);
    this.router.navigate([ROUTES.ADMIN_HOME]);
    return false;
  }

}
