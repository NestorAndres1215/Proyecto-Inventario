import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/core/services/login.service';
import { ProductoService } from 'src/app/core/services/producto.service';
import { SalidaService } from 'src/app/core/services/salida.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-registar-salida-usuario',
  templateUrl: './registar-salida-usuario.component.html',
  styleUrls: ['./registar-salida-usuario.component.css']
})
export class RegistarSalidaUsuarioComponent implements OnInit {


  fechaSalida: string = "";
  listaDetalleSalida: any[] = [];
  producto: any[] = [];
  isLoggedIn = false;
  user: any = null;
  detalleSalida: any = {

    descripcion: '',
    cantidad: '',

    producto: {
      productoId: '',
    },
    usuario: {
      id: '',
    },
    salida: {
      fechaSalida: '',
    },
  };




  constructor(
    private productoService: ProductoService,
    private login: LoginService,
    private salidaService: SalidaService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.obtenerProducto();
    this.obtenerUsuario();

  }

  enviarEntrada() {
    console.log(this.detalleSalida);

    //Verifica que los campos estén completos
    if (this.listaDetalleSalida.length > 0) {
      // Asegúrate de que this.fechaEntrada tenga un valor definido antes de usarlo

      // Itera sobre cada elemento del arreglo
      this.listaDetalleSalida.forEach((detalleSalida: any) => {
        detalleSalida.usuario.id = this.user.id;
      });

      // Llama a tu función para enviar la salida al servidor
      this.salidaService.crearSalidaConDetalles(this.listaDetalleSalida).subscribe({
        next: (response) => {
          console.log('Respuesta del servidor:', response);

          this.listaDetalleSalida = [];
          this.limpiar();

          swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: 'La salida se ha enviado correctamente',
          });

          this.router.navigate(['/user-dashboard/salidas-usuario']);
        },
        error: (error) => {
          console.error('Error al hacer la solicitud:', error);
          swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Hubo un problema al enviar la salida. Por favor, inténtalo de nuevo.',
          });
        },
        complete: () => {
          console.log('Petición finalizada.');
        }
      });

    } else {
      // Maneja el caso en el que los campos no estén completos
      console.error('Campos incompletos');
      // Puedes mostrar un mensaje de error al usuario o realizar otras acciones aquí
      swal.fire({
        icon: 'error',
        title: 'Campos incompletos',
        text: 'Por favor, completa todos los campos antes de enviar la salida.',
      });

    }
  }







  obtenerProducto(): void {
    this.productoService.listarProductosActivos().subscribe({
      next: (productos: any) => {
        this.producto = productos;
      },
      error: (error: any) => {
        console.error('Error al obtener los productos:', error);
      },
      complete: () => {
        console.log('Listado de productos cargado.');
      }
    });
  }

  obtenerUsuario() {
    this.isLoggedIn = this.login.isLoggedIn();
    this.user = this.login.getUser();
    this.login.loginStatusSubject.asObservable().subscribe(
      data => {
        this.isLoggedIn = this.login.isLoggedIn();
        this.user = this.login.getUser();
      }
    )
  }

  agregarProducto() {
    this.listaDetalleSalida.push({ ...this.detalleSalida });

    this.limpiar();


  }
  limpiar() {
    this.detalleSalida = {
      descripcion: '',
      cantidad: '',

      producto: {
        productoId: '',
      },
      usuario: {
        id: '',
      },
      salida: {
        fechaSalida: '',
      },
    }
  }

}
