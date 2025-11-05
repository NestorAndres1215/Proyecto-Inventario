import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Reclamo } from 'src/app/core/models/reclamo';
import { ReclamoService } from 'src/app/core/services/reclamo.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-responder-correo',
  templateUrl: './responder-correo.component.html',
  styleUrls: ['./responder-correo.component.css']
})
export class ResponderCorreoComponent implements OnInit {

  reclamoId!: number;
  reclamo!: Reclamo;
  mensaje: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reclamoService: ReclamoService
  ) {}

  ngOnInit(): void {
    this.reclamoId = Number(this.route.snapshot.paramMap.get('reclamoId'));
    this.obtenerReclamoPorId();
  }

  obtenerReclamoPorId(): void {
    this.reclamoService.obtenerReclamoPorId(this.reclamoId).subscribe({
      next: (data: Reclamo) => {
        this.reclamo = data;
      },
      error: (error) => {
        console.error(error);
        Swal.fire('Error', 'No se pudo obtener el reclamo.', 'error');
      }
    });
  }

  enviarDisculpas(): void {

    if (!this.mensaje.trim()) {
      Swal.fire('Error', 'El mensaje no puede estar vacío.', 'warning');
      return;
    }

    this.reclamoService.enviarDisculpas(this.reclamoId, this.mensaje).subscribe({
      next: () => {
        Swal.fire('Éxito', 'Las disculpas se enviaron correctamente.', 'success');
        this.mensaje = ''; // limpiar caja de texto
        this.router.navigate(['/user-dashboard/configuracion']);
      },
      error: (error) => {
        console.error(error);
        Swal.fire('Error', 'Hubo un problema al enviar las disculpas.', 'error');
      }
    });
  }

}
