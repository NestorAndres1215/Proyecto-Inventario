import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Reclamos } from 'src/app/core/models/reclamo';
import { LoginService } from 'src/app/core/services/login.service';
import { ReclamoService } from 'src/app/core/services/reclamo.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-guardar-reclamo',
  templateUrl: './guardar-reclamo.component.html',
  styleUrls: ['./guardar-reclamo.component.css']
})
export class GuardarReclamoComponent implements OnInit {

  isLoggedIn = false;
  user: any = null;
  reclamoForm!: FormGroup;

  constructor(
    private login: LoginService,
    private router: Router,
    private reclamoService: ReclamoService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.obtenerUsuario();
    this.crearFormulario();
  }

  crearFormulario(): void {
    this.reclamoForm = this.fb.group({
      asunto: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  enviarEntrada() {
    if (this.reclamoForm.invalid) {
      Swal.fire('Error', 'El asunto es obligatorio y debe tener al menos 10 caracteres.', 'error');
      return;
    }

    const entrada: Reclamos = {
      asunto: this.reclamoForm.value.asunto,
      usuario: this.user
    };

    this.reclamoService.agregarReclamo(entrada).subscribe(
      response => {
        Swal.fire('Éxito', 'La entrada se ha enviado correctamente.', 'success');
        this.reclamoForm.reset();
        this.router.navigate(['/user-dashboard/configuracion']);
      },
      error => {
        Swal.fire('Error', 'Hubo un problema al enviar la entrada. Por favor, inténtalo de nuevo.', 'error');
      }
    );
  }

  obtenerUsuario() {
    this.isLoggedIn = this.login.isLoggedIn();
    this.user = this.login.getUser();
    this.login.loginStatusSubject.asObservable().subscribe(() => {
      this.isLoggedIn = this.login.isLoggedIn();
      this.user = this.login.getUser();
    });
  }

}
