import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { ReclamoService } from 'src/app/core/services/reclamo.service';
import { AlertService } from 'src/app/core/services/alert.service';

@Component({
  selector: 'app-listar-reclamo-desactivados',
  templateUrl: './listar-reclamo-desactivados.component.html',
  styleUrls: ['./listar-reclamo-desactivados.component.css'],
})
export class ListarReclamoDesactivadosComponent implements OnInit {
  reclamos: any[] = [];
  cargando = false;

  columnas = [
    { clave: 'reclamoId', etiqueta: 'Código' },
    { clave: 'nombre', etiqueta: 'Nombre' },
    { clave: 'correo', etiqueta: 'Correo' },
    { clave: 'asunto', etiqueta: 'Asunto' },
    { clave: 'estado', etiqueta: 'Estado' },
  ];

  botonesConfigTable = {
    activar: true,
    textoActivar: 'Reactivar',
  };

  constructor(
    private reclamoService: ReclamoService,
    private router: Router,
    private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.obtenerReclamosDesactivados();
  }

  async obtenerReclamosDesactivados(): Promise<void> {
    this.cargando = true;

    try {
      this.reclamos = await firstValueFrom(
        this.reclamoService.listarReclamosDesactivados(),
      );
    } catch (error: any) {
      this.alertService.error(
        'Error',
        error.error?.message ?? 'No se pudieron cargar los reclamos.',
      );
    } finally {
      this.cargando = false;
    }
  }

  verReclamo(item: any): void {
    this.router.navigate([
      '/admin/configuracion/reclamos',
      item.id,
    ]);
  }
}