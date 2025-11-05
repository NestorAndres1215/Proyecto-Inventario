import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ProductoService } from 'src/app/core/services/producto.service';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import Swal from 'sweetalert2';
import { ReportesService } from 'src/app/core/services/reportes.service';

@Component({
  selector: 'app-listar-inventario',
  templateUrl: './listar-inventario.component.html',
  styleUrls: ['./listar-inventario.component.css']
})
export class ListarInventarioComponent implements OnInit {

  nombre: string = '';
  producto: any = [];
  categoriaId: string = '';
  proveedorId: string = '';
  productos: any[] = [];
  productoId: string = '';
  constructor(private http: HttpClient,
    private productoService: ProductoService,
    private reporteSalida: ReportesService,
    private router: Router) { }
  ngOnInit(): void {
    this.obtenerProducto();
  }

  obtenerProducto(): void {
    this.productoService.listarProductosActivos().subscribe({
      next: (productos: any[]) => {
        this.productos = productos;
      },
      error: (error: any) => {
        console.error("Error al obtener los productos:", error);
      },
      complete: () => {
        console.log("Productos cargados correctamente.");
      }
    });
  }

  pageSize = 4; // Tamaño de página (número de elementos por página)
  pageIndex = 0; // 
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  onPageChange(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }


  desactivarProducto(productoId: any): void {
    this.productoService.desactivarProducto(productoId).subscribe({
      next: (respuesta: any) => {
        Swal.fire({
          icon: 'success',
          title: 'Producto desactivado',
          text: respuesta?.mensaje || 'El producto fue desactivado correctamente.'
        });

        this.obtenerProducto(); // Volver a cargar la lista
      },
      error: (error: any) => {
        Swal.fire({
          icon: 'error',
          title: 'Error al desactivar el producto',
          text: error?.error?.mensaje || 'Ocurrió un error inesperado.'
        });
      },
      complete: () => {
        console.log("Petición para desactivar producto finalizada.");
      }
    });
  }


  buscarPorNombre() {
    try {
      if (this.nombre && this.productos) {
        this.productos = this.productos.filter((proveedor: any) =>
          proveedor.nombre.toLowerCase().includes(this.nombre.toLowerCase())
        );
      } else {
        this.restaurarProveedores();
        console.log("Ingrese un nombre o RUC para buscar.");
      }
    } catch (error) {
      console.log("Error en la búsqueda: ", error);
      // Realizar acciones de manejo de errores, como mostrar un mensaje al usuario
    }
  }

  restaurarProveedores(): void {
    this.nombre = ''; // Limpiar filtro / búsqueda

    this.productoService.listarProductosActivos().subscribe({
      next: (productos: any[]) => {
        this.productos = productos;
      },
      error: (error: any) => {
        console.error("Error al obtener los productos:", error);
      },
      complete: () => {
        console.log("Listado de productos restaurado correctamente.");
      }
    });
  }


  descargarPDF() {
    this.reporteSalida.descargarProducto().subscribe((data: Blob) => {
      const blob = new Blob([data], { type: 'application/pdf' });
      const urlBlob = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = urlBlob;
      a.download = 'informe_detalle_productos.pdf';
      a.style.display = 'none';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(urlBlob);
      document.body.removeChild(a);
    });
  }

}
