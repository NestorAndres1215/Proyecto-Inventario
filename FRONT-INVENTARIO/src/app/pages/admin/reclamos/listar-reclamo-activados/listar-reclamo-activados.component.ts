import { Component, OnInit } from '@angular/core';
import { ReclamoService } from 'src/app/core/services/reclamo.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-reclamo-activados',
  templateUrl: './listar-reclamo-activados.component.html',
  styleUrls: ['./listar-reclamo-activados.component.css']
})
export class ListarReclamoActivadosComponent implements OnInit {

  reclamos: any[] = [];
  cargando: boolean = false;

  constructor(private reclamoService: ReclamoService) { }

  ngOnInit(): void {
    this.obtenerReclamosActivos();
  }

  obtenerReclamosActivos() {
    this.cargando = true;

    this.reclamoService.listarReclamosActivos().subscribe({
      next: (data: any[]) => {
        this.reclamos = data;
        this.cargando = false;
      },
      error: (error) => {
        console.error("Error al obtener los reclamos activos:", error);
        this.cargando = false;
        Swal.fire("Error", "No se pudieron cargar los reclamos activos", "error");
      }
    });
  }
}
