import { DashboardComponent } from './features/admin/home-admin/home-admin.component';
import { LoginComponent } from './features/config/login/login.component';
import { HomeComponent } from './features/config/home/home.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PrincipalComponent } from './features/admin/principal/principal.component';
import { PerfilComponent } from './features/admin/perfil/perfil.component';
import { ProveedorComponent } from './features/admin/proveedor/proveedor.component';
import { ProductosComponent } from './features/admin/productos/productos.component';
import { ConfiguracionComponent } from './features/admin/configuracion/configuracion.component';
import { CrearProductoComponent } from './features/admin/productos/crear-producto/crear-producto.component';
import { ActualizarProductoComponent } from './features/admin/productos/actualizar-producto/actualizar-producto.component';
import { CrearProveedorComponent } from './features/admin/proveedor/crear-proveedor/crear-proveedor.component';
import { ActualizarProveedorComponent } from './features/admin/proveedor/actualizar-proveedor/actualizar-proveedor.component';
import { HomeUsuarioComponent } from './features/usuario/home-usuario/home-usuario.component';
import { RegistrarUsuarioAdministradorComponent } from './features/admin/usuario/usuario-administrador/registrar-usuario-administrador/registrar-usuario-administrador.component';
import { RegistrarUsuarioOperadorComponent } from './features/admin/usuario/usuario-operador/registrar-usuario-operador/registrar-usuario-operador.component';
import { DetalleUsuarioComponent } from './features/admin/usuario/detalle-usuario/detalle-usuario.component';
import { DetalleProveedorComponent } from './features/admin/proveedor/detalle-proveedor/detalle-proveedor.component';
import { DetalleProductoComponent } from './features/admin/productos/detalle-producto/detalle-producto.component';
import { PrincipalUsuarioComponent } from './features/usuario/principal-usuario/principal-usuario.component';
import { InventarioUsuarioComponent } from './pages/usuario/inventario-usuario/inventario-usuario.component';
import { ConfiguracionUsuarioComponent } from './features/usuario/configuracion-usuario/configuracion-usuario.component';
import { EntradasComponent } from './pages/admin/entradas/entradas.component';
import { RegistrarEntradaComponent } from './pages/admin/entradas/registrar-entrada/registrar-entrada.component';
import { DetalleEntradaComponent } from './pages/admin/entradas/detalle-entrada/detalle-entrada.component';
import { SalidasComponent } from './features/admin/salidas/salidas.component';
import { RegistrarSalidasComponent } from './features/admin/salidas/registrar-salidas/registrar-salidas.component';
import { DetalleSalidasComponent } from './features/admin/salidas/detalle-salidas/detalle-salidas.component';
import { EntradaUsuarioComponent } from './pages/usuario/entrada-usuario/entrada-usuario.component';
import { SalidaUsuarioComponent } from './features/usuario/salida-usuario/salida-usuario.component';
import { ActualizarUsuarioComponent } from './features/admin/usuario/actualizar-usuario/actualizar-usuario.component';
import { ResponderCorreoComponent } from './features/admin/reclamos/responder-correo/responder-correo.component';
import { DetalleEntradaUsuarioComponent } from './pages/usuario/entrada-usuario/detalle-entrada-usuario/detalle-entrada-usuario.component';
import { ActualizarInventarioComponent } from './pages/usuario/inventario-usuario/actualizar-inventario/actualizar-inventario.component';
import { GuardarInventarioComponent } from './pages/usuario/inventario-usuario/guardar-inventario/guardar-inventario.component';
import { RegistrarEntradaUsuarioComponent } from './pages/usuario/entrada-usuario/registrar-entrada-usuario/registrar-entrada-usuario.component';
import { AdminGuard } from './core/guards/admin.guard';
import { NormalGuard } from './core/guards/normal.guard';
import { ListaUsuarioAdministradorDesactivadosComponent } from './features/admin/usuario/usuario-administrador/lista-usuario-administrador-desactivados/lista-usuario-administrador-desactivados.component';
import { UsuarioAdministradorComponent } from './features/admin/usuario/usuario-administrador/usuario-administrador.component';
import { UsuarioOperadorComponent } from './features/admin/usuario/usuario-operador/usuario-operador.component';
import { ListaUsuarioOperadorDesactivadosComponent } from './features/admin/usuario/usuario-operador/lista-usuario-operador-desactivados/lista-usuario-operador-desactivados.component';
import { RegistarSalidaUsuarioComponent } from './features/usuario/salida-usuario/registar-salida-usuario/registar-salida-usuario.component';
import { ActualizarUsuarioUsuarioComponent } from './pages/usuario/actualizar-usuario-usuario/actualizar-usuario-usuario.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full',
  },

  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full',
  },
  {
    path: 'admin',
    component: DashboardComponent,

    canActivate: [AdminGuard],
    children: [
      {
        path: '',
        component: PrincipalComponent,
      },
      {
        path: 'perfil',
        component: PerfilComponent,
      },

      {
        path: 'salidas',
        component: SalidasComponent,
      },
      {
        path: 'salidas/registrar-salidas',
        component: RegistrarSalidasComponent,
      },
      {
        path: 'salidas/detalle/:detalleSalidaId',
        component: DetalleSalidasComponent,
      },

      {
        path: 'entradas',
        component: EntradasComponent,
      },
      {
        path: 'entradas/registrar-entradas',
        component: RegistrarEntradaComponent,
      },
      {
        path: 'entradas/detalle/:detalleEntradaId',
        component: DetalleEntradaComponent,
      },

      {
        path: 'proveedor',
        component: ProveedorComponent,
      },
      {
        path: 'proveedor/crear-proveedor',
        component: CrearProveedorComponent,
      },
      {
        path: 'proveedor/detalle/:proveedorId',
        component: DetalleProveedorComponent,
      },
      {
        path: 'proveedor/:proveedorId',
        component: ActualizarProveedorComponent,
      },

      {
        path: 'producto',
        component: ProductosComponent,
      },
      {
        path: 'producto/crear-producto',
        component: CrearProductoComponent,
      },
      {
        path: 'producto/detalle/:productoId',
        component: DetalleProductoComponent,
      },
      {
        path: 'producto/:productoId',
        component: ActualizarProductoComponent,
      },

      {
        path: 'usuario/operador',
        component: UsuarioOperadorComponent,
      },
      {
        path: 'usuario/administrador',
        component: UsuarioAdministradorComponent,
      },
      {
        path: 'usuario/registrar-usuario-administrador',
        component: RegistrarUsuarioAdministradorComponent,
      },
      {
        path: 'usuario/registrar-usuario-operador',
        component: RegistrarUsuarioOperadorComponent,
      },
      {
        path: 'usuario/usuarios-admin-desactivados',
        component: ListaUsuarioAdministradorDesactivadosComponent,
      },
      {
        path: 'usuario/usuarios-operador-desactivados',
        component: ListaUsuarioOperadorDesactivadosComponent,
      },
      {
        path: 'usuario/:id',
        component: DetalleUsuarioComponent,
      },

      {
        path: 'configuracion',
        component: ConfiguracionComponent,
      },
      {
        path: 'configuracion/reclamos/:reclamoId',
        component: ResponderCorreoComponent,
      },
      {
        path: 'configuracion/actualizar-usuario/:id',
        component: ActualizarUsuarioComponent,
      },
    ],
  },
  {
    path: 'user-dashboard',
    component: HomeUsuarioComponent,

    canActivate: [NormalGuard],
    children: [
      {
        path: '',
        component: PrincipalUsuarioComponent,
      },
      {
        path: 'salidas/detalle/:detalleSalidaId',
        component: DetalleSalidasComponent,
      },
      {
        path: 'inventario',
        component: InventarioUsuarioComponent,
      },
      {
        path: 'inventario/crear-producto',
        component: GuardarInventarioComponent,
      },
      {
        path: 'inventario/:productoId',
        component: ActualizarInventarioComponent,
      },
      {
        path: 'entradas-usuario',
        component: EntradaUsuarioComponent,
      },
      {
        path: 'entradas-usuario/registrar-entradas',
        component: RegistrarEntradaUsuarioComponent,
      },
      {
        path: 'salidas-usuario',
        component: SalidaUsuarioComponent,
      },
      {
        path: 'salidas/registrar-salidas',
        component: RegistarSalidaUsuarioComponent,
      },

      {
        path: 'configuracion',
        component: ConfiguracionUsuarioComponent,
      },

      {
        path: 'entradas/detalle/:detalleEntradaId',
        component: DetalleEntradaUsuarioComponent,
      },
      {
        path: 'configuracion/actualizar-usuario/:id',
        component: ActualizarUsuarioUsuarioComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
