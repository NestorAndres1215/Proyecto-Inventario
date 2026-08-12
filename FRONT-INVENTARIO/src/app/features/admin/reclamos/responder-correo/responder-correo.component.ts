import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Reclamo } from 'src/app/core/models/reclamo';
import { AlertService } from 'src/app/core/services/alert.service';
import { ReclamoService } from 'src/app/core/services/reclamo.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-responder-correo',
  templateUrl: './responder-correo.component.html',
  styleUrls: ['./responder-correo.component.css'],
})
export class ResponderCorreoComponent implements OnInit {
  reclamoId!: number;
  reclamo!: Reclamo;
  mensaje: string = '';

  constructor(
    private route: ActivatedRoute,
    private alertService: AlertService,
    private router: Router,
    private reclamoService: ReclamoService,
  ) {}

  ngOnInit(): void {
    this.reclamoId = Number(this.route.snapshot.paramMap.get('reclamoId'));
    this.obtenerReclamoPorId();
  }

  async obtenerReclamoPorId(): Promise<void> {
    try {
      this.reclamo = await firstValueFrom(
        this.reclamoService.obtenerReclamoPorId(this.reclamoId),
      );
    } catch (error) {
      console.error(error);

      this.alertService.error('Error', 'No se pudo obtener el reclamo.');
    }
  }
  async enviarDisculpas(): Promise<void> {
    if (!this.mensaje.trim()) {
      this.alertService.advertencia(
        'Error',
        'El mensaje no puede estar vacío.',
      );
      return;
    }

    try {
      await firstValueFrom(
        this.reclamoService.enviarDisculpas(this.reclamoId, this.mensaje),
      );

      this.alertService.aceptacion(
        'Éxito',
        'Las disculpas se enviaron correctamente.',
      );

      this.mensaje = '';
      this.router.navigate(['/user-dashboard/configuracion']);
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error?.error?.message ?? 'Hubo un problema al enviar las disculpas.',
      );
    }
  }
}
