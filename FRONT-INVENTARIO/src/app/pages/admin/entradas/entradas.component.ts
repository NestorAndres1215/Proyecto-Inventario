import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';
import { EntradaService } from 'src/app/core/services/entrada.service';
import { ReportesService } from 'src/app/core/services/reportes.service';

@Component({
  selector: 'app-entradas',
  templateUrl: './entradas.component.html',
  styleUrls: ['./entradas.component.css']
})
export class EntradasComponent implements OnInit {

  detalleEntrada: any[] = [];

  columnas = [
    { clave: 'detalleEntradaId', etiqueta: 'Código' },
    { clave: 'producto.nombre', etiqueta: 'Producto' },
    { clave: 'descripcion', etiqueta: 'Descripción' },
    { clave: 'cantidad', etiqueta: 'Cantidad' },
    { clave: 'entrada.fechaEntrada', etiqueta: 'Fecha de Salida' },
    { clave: 'usuario.nombre', etiqueta: 'Responsable' }
  ];

  botonesConfig = {
    ver: true
  };

  constructor(
    private readonly entradaService: EntradaService,
    private readonly router: Router,
    private readonly reportesService: ReportesService,
    private readonly alertService: AlertService
  ) {}

  async ngOnInit(): Promise<void> {
    await this.obtenerEntradas();
  }

  verDetalle(item: any): void {
    this.router.navigate(['/admin/entradas/detalle', item.detalleSalidaId]);
  }

  private async obtenerEntradas(): Promise<void> {
    try {
      this.detalleEntrada = await firstValueFrom(
        this.entradaService.listarEntradas()
      );
    } catch (error) {
      console.error('Error al obtener las entradas:', error);

      this.alertService.error(
        'Error',
        'No se pudieron cargar las entradas.'
      );
    }
  }

  async descargarPDF(): Promise<void> {
    try {
      const data = await firstValueFrom(
        this.reportesService.descargarEntrada()
      );

      this.generarPDF(data);
    } catch (error) {
      console.error('Error al descargar el reporte:', error);

      this.alertService.error(
        'Error',
        'No se pudo descargar el reporte.'
      );
    }
  }

  private generarPDF(data: Blob): void {
    const url = window.URL.createObjectURL(
      new Blob([data], { type: 'application/pdf' })
    );

    const enlace = document.createElement('a');
    enlace.href = url;
    enlace.download = 'informe_detalle_entradas_productos.pdf';
    enlace.click();

    window.URL.revokeObjectURL(url);
  }
}