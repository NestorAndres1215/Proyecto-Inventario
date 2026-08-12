import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { SalidaService } from 'src/app/core/services/salida.service';

@Component({
  selector: 'app-detalle-salidas',
  templateUrl: './detalle-salidas.component.html',
  styleUrls: ['./detalle-salidas.component.css'],
})
export class DetalleSalidasComponent implements OnInit {
  detalleSalida: any;
  detalleSalidaId: any = 0;
  datosUsuario: { clave: string; valor: any }[] = [];

  constructor(
    private salidaService: SalidaService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.detalleSalidaId = this.route.snapshot.params['detalleSalidaId'];
    this.obtenerSalidaId(this.detalleSalidaId);
  }

  async obtenerSalidaId(detalleSalidaId: number): Promise<void> {
    const data = await firstValueFrom(
      this.salidaService.obtenerSalidaPorId(detalleSalidaId),
    );

    this.detalleSalida = data;

    this.datosUsuario = [
      { clave: 'Código', valor: data.detalleSalidaId },
      { clave: 'Producto', valor: data.producto?.nombre },
      { clave: 'Descripción', valor: data.descripcion },
      { clave: 'Cantidad', valor: data.cantidad },
      { clave: 'Stock', valor: data.producto?.stock },
      { clave: 'Fecha de salida', valor: data.salida?.fechaSalida },
      { clave: 'Usuario', valor: data.usuario?.username },
      {
        clave: 'Nombre',
        valor: `${data.usuario?.nombre} ${data.usuario?.apellido}`,
      },
    ];
  }
}
