import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';
import { EntradaService } from 'src/app/core/services/entrada.service';

@Component({
  selector: 'app-detalle-entrada',
  templateUrl: './detalle-entrada.component.html',
  styleUrls: ['./detalle-entrada.component.css'],
})
export class DetalleEntradaComponent implements OnInit {
  detalleEntrada: any = null;
  detalleEntradaId = 0;

  constructor(
    private readonly entradaService: EntradaService,
    private readonly alertService: AlertService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) {}

  async ngOnInit(): Promise<void> {
    this.detalleEntradaId = Number(
      this.route.snapshot.paramMap.get('detalleEntradaId'),
    );

    if (!this.detalleEntradaId) {
      this.router.navigate(['/admin/entradas']);
      return;
    }

    await this.cargarEntrada(this.detalleEntradaId);
  }

  private async cargarEntrada(id: number): Promise<void> {
    try {
      this.detalleEntrada = await firstValueFrom(
        this.entradaService.obtenerEntradaPorId(id),
      );
    } catch (error) {
      this.alertService.error('Error', 'No se pudo cargar la entrada.');

      this.router.navigate(['/admin/entradas']);
    }
  }
}
