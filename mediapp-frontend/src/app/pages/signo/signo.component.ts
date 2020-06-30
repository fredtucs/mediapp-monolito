import { Component, OnInit, ViewChild } from '@angular/core';
import { Signo } from './../../_model/signo';
import { SignoService } from 'src/app/_service/signo.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { switchMap, tap } from 'rxjs/operators';

@Component({
  selector: 'app-signo',
  templateUrl: './signo.component.html',
  styleUrls: ['./signo.component.css']
})
export class SignoComponent implements OnInit {

  isLoadingResults = true;
  cantidad = 0;
  displayedColumns: string[] = ['paciente', 'temperatura', 'fecha', 'acciones'];
  dataSource: MatTableDataSource<Signo>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private signoService: SignoService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {

    this.signoService.signoCambio.subscribe(data => {
      this.dataSource = new MatTableDataSource(data);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });

    this.signoService.mensajeCambio.subscribe(data => {
      this.snackBar.open(data, 'AVISO', {
        duration: 2000
      });
    });

    this.signoService.listarPageable(0, 10).subscribe(data => {
      this.cantidad = data.totalElements;
      this.dataSource = new MatTableDataSource(data.content);
    });

  }

  filtrar(valor: string) {
    this.dataSource.filter = valor.trim().toLowerCase();
  }

  eliminar(idSigno: number) {
    this.signoService.eliminar(idSigno).pipe(switchMap(() => {
      return this.signoService.listar();
    })).subscribe(data => {
      this.signoService.signoCambio.next(data);
      this.signoService.mensajeCambio.next('SE ELIMINO');
    });
  }

  mostrarMas(e: any) {
    this.signoService.listarPageable(e.pageIndex, e.pageSize).pipe(
      tap(() => this.isLoadingResults = false)
    ).subscribe(data => {
      this.cantidad = data.totalElements;
      this.dataSource = new MatTableDataSource(data.content);
      this.dataSource.sort = this.sort;
      this.isLoadingResults = true;
    });
  }

}
