import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UsuarioService } from 'src/app/core/services/usuario.service';

@Component({
  selector: 'app-detalle-usuario',
  templateUrl: './detalle-usuario.component.html',
  styleUrls: ['./detalle-usuario.component.css']
})
export class DetalleUsuarioComponent implements OnInit {

  usuarioRol: any;
  usuarioRolId!: number;

  constructor(
    private usuarioService: UsuarioService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.usuarioRolId = Number(this.route.snapshot.params['id']);
    this.obtenerUsuarioPorId(this.usuarioRolId);
  }

  obtenerUsuarioPorId(usuarioRolId: number): void {
    this.usuarioService.obtenerUsuarioPorId(usuarioRolId)
      .subscribe({
        next: (data) => {
          this.usuarioRol = data;
        },
        error: (error) => {
          console.error('Error al obtener el usuario:', error);
        }
      });
  }
}
