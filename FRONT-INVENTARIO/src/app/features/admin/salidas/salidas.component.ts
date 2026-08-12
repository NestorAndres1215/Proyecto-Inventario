import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ReportesService } from 'src/app/core/services/reportes.service';
import { SalidaService } from 'src/app/core/services/salida.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-salidas',
  templateUrl: './salidas.component.html',
  styleUrls: ['./salidas.component.css'],
})
export class SalidasComponent implements OnInit {
  salidas: any[] = [];
  detalleSalida: any;
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
  verDetalle(item: any): void {
    this.router.navigate(['/admin/salidas/detalle', item.detalleSalidaId]);
  }
  constructor(
    private salidaService: SalidaService,
    private router: Router,
    private reporteSalida: ReportesService,
  ) {}

  ngOnInit(): void {
    this.listarSalidas();
  }
  async listarSalidas(): Promise<void> {
    this.salidas = await firstValueFrom(this.salidaService.listarSalidas());
  }

  async descargarPDF(): Promise<void> {
    const data = await firstValueFrom(this.reporteSalida.descargarSalida());

    const url = window.URL.createObjectURL(data);
    const link = document.createElement('a');

    link.href = url;
    link.download = 'informe_detalle_salidas_productos.pdf';
    link.click();

    window.URL.revokeObjectURL(url);
  }
}
