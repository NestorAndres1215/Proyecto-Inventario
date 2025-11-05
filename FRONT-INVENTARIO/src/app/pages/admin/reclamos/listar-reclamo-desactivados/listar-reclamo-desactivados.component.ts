import { Component, OnInit } from '@angular/core';
import { ReclamoService } from 'src/app/core/services/reclamo.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-reclamo-desactivados',
  templateUrl: './listar-reclamo-desactivados.component.html',
  styleUrls: ['./listar-reclamo-desactivados.component.css']
})
export class ListarReclamoDesactivadosComponent implements OnInit {

  reclamos: any[] = [];
  cargando: boolean = false;

  constructor(private reclamoService: ReclamoService) { }

  ngOnInit(): void {
    this.obtenerReclamosDesactivados();
  }

  obtenerReclamosDesactivados() {
    this.cargando = true;

    this.reclamoService.listarReclamosDesactivados().subscribe({
      next: (data: any[]) => {
        this.reclamos = data;
        this.cargando = false;
      },
      error: (error) => {
        console.error("Error al obtener los reclamos:", error);
        this.cargando = false;
        Swal.fire("Error", "No se pudieron cargar los reclamos desactivados", "error");
      }
    });
  }

}
