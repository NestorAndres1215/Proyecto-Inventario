import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from 'src/app/core/services/login.service';
import { ROUTES } from 'src/app/core/constants/routes.constants';
import { MESSAGES } from 'src/app/core/constants/messages.constants';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(
    private loginService: LoginService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const isLogged = this.loginService.isLoggedIn();

    if (isLogged) {
      // Usuario autenticado: impedir acceso a la página de login
      if (state.url === ROUTES.LOGIN) {
        console.info(MESSAGES.REDIRECT_LOGIN);
        this.router.navigate([ROUTES.ADMIN_HOME]);
        return false;
      }
      return true;
    }

    // Usuario no autenticado
    console.warn(MESSAGES.UNAUTHORIZED_ACCESS);
    this.router.navigate([ROUTES.USER_DASHBOARD]);
    return false;
  }

}
