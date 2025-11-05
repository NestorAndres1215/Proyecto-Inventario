import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DetalleSalida } from 'src/app/core/models/detalle-salidad';
import { LoginService } from 'src/app/core/services/login.service';
import { ProductoService } from 'src/app/core/services/producto.service';
import { SalidaService } from 'src/app/core/services/salida.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-registrar-salidas',
  templateUrl: './registrar-salidas.component.html',
  styleUrls: ['./registrar-salidas.component.css']
})
export class RegistrarSalidasComponent implements OnInit {

  salidaForm!: FormGroup;
  producto: any[] = [];
  listaDetalleSalida: any[] = [];
  isLoggedIn = false;
  user: any = null;

  constructor(
    private fb: FormBuilder,
    private productoService: ProductoService,
    private login: LoginService,
    private salidaService: SalidaService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.salidaForm = this.fb.group({
      productoId: ['', Validators.required],
      descripcion: ['', Validators.required],
      cantidad: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      fechaSalida: ['', Validators.required]
    });

    this.obtenerProducto();
    this.obtenerUsuario();
  }

  obtenerProducto() {
    this.productoService.listarProductosActivos().subscribe({
      next: (data: any) => this.producto = data,
      error: (err) => console.error(err)
    });
  }

  obtenerUsuario() {
    this.isLoggedIn = this.login.isLoggedIn();
    this.user = this.login.getUser();
    this.login.loginStatusSubject.asObservable().subscribe(() => {
      this.isLoggedIn = this.login.isLoggedIn();
      this.user = this.login.getUser();
    });
  }

  agregarProducto() {
    if (this.salidaForm.invalid) {
      Swal.fire('Error', 'Complete todos los campos antes de agregar.', 'error');
      return;
    }

    const detalle: DetalleSalida = {
      producto: { productoId: this.salidaForm.value.productoId },
      descripcion: this.salidaForm.value.descripcion,
      cantidad: this.salidaForm.value.cantidad,
      salida: { fechaSalida: this.salidaForm.value.fechaSalida },
      usuario: { id: this.user.id }
    };

    this.listaDetalleSalida.push(detalle);
    this.salidaForm.reset();
  }

  enviarSalida() {
    if (this.listaDetalleSalida.length === 0) {
      Swal.fire('Error', 'Agregue al menos un registro antes de enviar.', 'error');
      return;
    }

    this.salidaService.crearSalidaConDetalles(this.listaDetalleSalida).subscribe({
      next: () => {
        Swal.fire('Éxito', 'La salida se ha enviado correctamente', 'success');
        this.listaDetalleSalida = [];
        this.salidaForm.reset();
        this.router.navigate(['/admin/salidas']);
      },
      error: (err) => {
        console.error(err);
        Swal.fire('Error', 'Hubo un problema al enviar la salida', 'error');
      }
    });
  }

}
