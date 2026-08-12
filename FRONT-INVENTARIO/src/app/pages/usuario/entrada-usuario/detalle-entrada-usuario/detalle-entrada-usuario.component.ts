import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';
import { EntradaService } from 'src/app/core/services/entrada.service';

@Component({
  selector: 'app-detalle-entrada-usuario',
  templateUrl: './detalle-entrada-usuario.component.html',
  styleUrls: ['./detalle-entrada-usuario.component.css'],
})
export class DetalleEntradaUsuarioComponent implements OnInit {
  detalleEntrada: any;
  detalleEntradaId = 0;

  constructor(
    private readonly entradaService: EntradaService,
    private readonly alertService: AlertService,
    private readonly route: ActivatedRoute,
  ) {}

  async ngOnInit(): Promise<void> {
    this.detalleEntradaId = Number(
      this.route.snapshot.paramMap.get('detalleEntradaId'),
    );

    await this.obtenerEntradaId(this.detalleEntradaId);
  }

  async obtenerEntradaId(detalleEntradaId: number): Promise<void> {
    try {
      this.detalleEntrada = await firstValueFrom(
        this.entradaService.obtenerEntradaPorId(detalleEntradaId),
      );
    } catch (error) {

      this.alertService.error(
        'Error',
        'No se pudo obtener el detalle de la entrada.',
      );
    }
  }
}
