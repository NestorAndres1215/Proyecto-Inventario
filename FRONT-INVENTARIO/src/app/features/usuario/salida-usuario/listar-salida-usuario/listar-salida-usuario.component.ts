import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AlertService } from 'src/app/core/services/alert.service';
import { ReportesService } from 'src/app/core/services/reportes.service';
import { SalidaService } from 'src/app/core/services/salida.service';

@Component({
  selector: 'app-listar-salida-usuario',
  templateUrl: './listar-salida-usuario.component.html',
  styleUrls: ['./listar-salida-usuario.component.css'],
})
export class ListarSalidaUsuarioComponent implements OnInit {
  detalleSalida: any[] = [];

  columnas = [
    { clave: 'detalleSalidaId', etiqueta: 'Código' },
    { clave: 'producto.nombre', etiqueta: 'Producto' },
    { clave: 'descripcion', etiqueta: 'Descripción' },
    { clave: 'cantidad', etiqueta: 'Cantidad' },
    { clave: 'salida.fechaSalida', etiqueta: 'Fecha de Salida' },
    { clave: 'usuario.nombre', etiqueta: 'Responsable' },
  ];

  botonesConfig = {
    ver: true,
  };

  constructor(
    private readonly router: Router,
    private readonly salidaService: SalidaService,
    private readonly reporteSalida: ReportesService,
    private readonly alertService: AlertService,
  ) {}

  async ngOnInit(): Promise<void> {
    await this.obtenerSalida();
  }

  verDetalle(item: any): void {
    this.router.navigate([
      '/user-dashboard/salidas/detalle',
      item.detalleSalidaId,
    ]);
  }

  async obtenerSalida(): Promise<void> {
    try {
      this.detalleSalida = await firstValueFrom(
        this.salidaService.listarSalidas(),
      );
    } catch (error) {

      this.alertService.error('Error', 'No se pudieron cargar las salidas.');
    }
  }

  async descargarPDF(): Promise<void> {
    try {
      const data = await firstValueFrom(this.reporteSalida.descargarSalida());

      const url = window.URL.createObjectURL(
        new Blob([data], { type: 'application/pdf' }),
      );

      const enlace = document.createElement('a');
      enlace.href = url;
      enlace.download = 'informe_detalle_salidas_productos.pdf';
      enlace.click();

      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error al descargar el reporte:', error);

      this.alertService.error('Error', 'No se pudo descargar el reporte.');
    }
  }
}
