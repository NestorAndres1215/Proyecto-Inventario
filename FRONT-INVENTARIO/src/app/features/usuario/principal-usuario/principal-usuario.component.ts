import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { LoginService } from 'src/app/core/services/login.service';

@Component({
  selector: 'app-principal-usuario',
  templateUrl: './principal-usuario.component.html',
  styleUrls: ['./principal-usuario.component.css']
})
export class PrincipalUsuarioComponent implements OnInit, OnDestroy {

  isLoggedIn = false;
  user: any = null;

  private readonly destroy$ = new Subject<void>();

  constructor(private readonly loginService: LoginService) {}

  ngOnInit(): void {
    this.cargarUsuario();

    this.loginService.loginStatusSubject
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.cargarUsuario());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private cargarUsuario(): void {
    this.isLoggedIn = this.loginService.isLoggedIn();
    this.user = this.loginService.getUser();
  }
}