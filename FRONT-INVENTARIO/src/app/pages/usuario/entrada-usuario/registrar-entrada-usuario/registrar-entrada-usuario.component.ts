import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EntradaService } from 'src/app/core/services/entrada.service';
import { LoginService } from 'src/app/core/services/login.service';
import { ProductoService } from 'src/app/core/services/producto.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-registrar-entrada-usuario',
  templateUrl: './registrar-entrada-usuario.component.html',
  styleUrls: ['./registrar-entrada-usuario.component.css']
})
export class RegistrarEntradaUsuarioComponent implements OnInit {

  cfechaEntrada: string = "";
  listaDetalleEntrada: any[] = [];
  producto: any[] = [];
  isLoggedIn = false;
  user: any = null;

  detalleEntrada: any = {

    descripcion: '',
    cantidad: '',

    producto: {
      productoId: '',
    },
    usuario: {
      id: '',
    },
    entrada: {
      fechaEntrada: '',
    },
  };

  constructor(
    private productoService: ProductoService,
    private login: LoginService,
    private entradaService: EntradaService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.obtenerProducto();
    this.obtenerUsuario();

  }
  enviarEntrada() {
    console.log(this.detalleEntrada);





    //Verifica que los campos estén completos
    if (this.listaDetalleEntrada.length > 0) {
      // Asegúrate de que this.fechaEntrada tenga un valor definido antes de usarlo




      // Itera sobre cada elemento del arreglo
      this.listaDetalleEntrada.forEach((detalleEntrada: any) => {
        detalleEntrada.usuario.id = this.user.id;
      });

      // Llama a tu función para enviar la entrada al servidor
      this.entradaService.crearEntradaConDetalles(this.listaDetalleEntrada)
        .subscribe((response) => {
          console.log('Respuesta del servidor:', response);
          this.listaDetalleEntrada = [];
          this.limpiar();

          swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: 'La entrada se ha enviado correctamente',
          });
          this.router.navigate(['/user-dashboard/entradas-usuario']);

          // Puedes manejar la respuesta del servidor aquí (por ejemplo, mostrar un mensaje de éxito al usuario)
        }, (error) => {
          console.error('Error al hacer la solicitud:', error);
          swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Hubo un problema al enviar la entrada. Por favor, inténtalo de nuevo.',
          });
        });
    } else {
      // Maneja el caso en el que los campos no estén completos
      console.error('Campos incompletos');
      // Puedes mostrar un mensaje de error al usuario o realizar otras acciones aquí
      swal.fire({
        icon: 'error',
        title: 'Campos incompletos',
        text: 'Por favor, completa todos los campos antes de enviar la entrada.',
      });

    }
  }



obtenerProducto() {
  this.productoService.listarProductosActivos().subscribe({
    next: (productos: any[]) => {
      this.producto = productos;
    },
    error: (err: any) => {
      console.error("Error al obtener los productos:", err);
    }
  });
}

obtenerUsuario() {
  this.isLoggedIn = this.login.isLoggedIn();
  this.user = this.login.getUser();

  this.login.loginStatusSubject.asObservable().subscribe({
    next: () => {
      this.isLoggedIn = this.login.isLoggedIn();
      this.user = this.login.getUser();
    },
    error: (err) => {
      console.error("Error al obtener el estado de sesión:", err);
    }
  });
}


  agregarProducto() {
    this.listaDetalleEntrada.push({ ...this.detalleEntrada });
    this.limpiar();
  }
  limpiar() {
    this.detalleEntrada = {
      descripcion: '',
      cantidad: null,

      producto: {
        productoId: '',
      },
      usuario: {
        id: '',
      },
      entrada: {
        fechaEntrada: '',
      },
    }

  }
 
guardarValor(event: any) {
  const input = event.target as HTMLInputElement;
  let value = input.value;

  // Permitir solo números y el signo "-"
  value = value.replace(/[^0-9-]/g, '');

  // Si el valor contiene más de un "-", eliminar extras
  const hasDash = value.startsWith('-');
  value = value.replace(/-/g, '');
  if (hasDash) {
    value = '-' + value;
  }

  // Actualizar el campo
  input.value = value;
}


}
