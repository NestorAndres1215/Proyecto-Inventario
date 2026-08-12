import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { LoginService } from '../../../core/services/login.service';

@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.css'],
})
export class PrincipalComponent implements OnInit, OnDestroy {
  isLoggedIn = false;
  user: any | null = null;

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
