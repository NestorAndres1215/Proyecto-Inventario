import { Component, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { EntradaService } from 'src/app/core/services/entrada.service';
import { ReportesService } from 'src/app/core/services/reportes.service';
import { AlertService } from 'src/app/core/services/alert.service';

@Component({
  selector: 'app-listar-entradas-usuario',
  templateUrl: './listar-entradas-usuario.component.html',
  styleUrls: ['./listar-entradas-usuario.component.css'],
})
export class ListarEntradasUsuarioComponent implements OnInit {
  detalleEntrada: any[] = [];

  constructor(
    private readonly entradaService: EntradaService,
    private readonly reporteSalida: ReportesService,
    private readonly alertService: AlertService,
  ) {}

  async ngOnInit(): Promise<void> {
    await this.obtenerEntradas();
  }

  async obtenerEntradas(): Promise<void> {
    try {
      this.detalleEntrada = await firstValueFrom(
        this.entradaService.listarEntradas(),
      );
    } catch (error) {
      this.alertService.error('Error', 'No se pudieron cargar las entradas.');
    }
  }

  async descargarPDF(): Promise<void> {
    try {
      const data = await firstValueFrom(this.reporteSalida.descargarEntrada());

      const blob = new Blob([data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);

      const enlace = document.createElement('a');
      enlace.href = url;
      enlace.download = 'informe_detalle_entradas_productos.pdf';
      enlace.click();

      window.URL.revokeObjectURL(url);
    } catch (error) {
      this.alertService.error('Error', 'No se pudo descargar el reporte.');
    }
  }
}
