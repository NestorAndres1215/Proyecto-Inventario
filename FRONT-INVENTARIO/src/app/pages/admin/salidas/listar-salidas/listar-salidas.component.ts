import { Component, OnInit } from '@angular/core';
import { SalidaService } from 'src/app/core/services/salida.service';
import { ReportesService } from 'src/app/core/services/reportes.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-salidas',
  templateUrl: './listar-salidas.component.html',
  styleUrls: ['./listar-salidas.component.css']
})
export class ListarSalidasComponent implements OnInit {

  salidas: any[] = [];

  constructor(
    private salidaService: SalidaService,
    private reporteSalida: ReportesService
  ) { }

  ngOnInit(): void {
    this.listarSalidas();
  }

  listarSalidas() {
    this.salidaService.listarSalidas().subscribe({
      next: (data: any[]) => {
        this.salidas = data;
      },
      error: (error) => {
        console.error("Error al obtener las salidas:", error);
        Swal.fire("Error", "No se pudieron cargar las salidas", "error");
      }
    });
  }

  descargarPDF() {
    this.reporteSalida.descargarSalida().subscribe({
      next: (data: Blob) => {
        const url = window.URL.createObjectURL(data);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'informe_detalle_salidas_productos.pdf';
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => {
        Swal.fire("Error", "No se pudo generar el PDF", "error");
      }
    });
  }
}
