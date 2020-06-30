import { PacienteDialogComponent } from './../../paciente/paciente-dialog/paciente-dialog.component';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { switchMap, tap } from 'rxjs/operators';
import { Paciente } from 'src/app/_model/paciente';
import { SignoService } from 'src/app/_service/signo.service';
import { MatDialog } from '@angular/material/dialog';

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-signo-edicion',
  templateUrl: './signo-edicion.component.html',
  styleUrls: ['./signo-edicion.component.css']
})
export class SignoEdicionComponent implements OnInit {

  pacientesFiltrados: any;
  errorMsg: string;

  signoForm: FormGroup;
  isLoadingResults = false;
  matcher = new MyErrorStateMatcher();

  id: number;
  edicion: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private signoService: SignoService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {

    this.signoForm = this.formBuilder.group({
      paciente: new FormControl('', [Validators.required]),
      fecha: [new Date(), Validators.required],
      temperatura: [null, Validators.required],
      pulso: [null, Validators.required],
      ritmoRespiratorio: [null, Validators.required]
    });

    this.signoForm.get('paciente').valueChanges.pipe(tap(() => this.isLoadingResults = true))
      .subscribe(val => {
        if (val && typeof val !== 'object' && val.length > 1) {
          this.signoService.buscarPaciente(val).subscribe(results => {
            this.pacientesFiltrados = results;
            this.isLoadingResults = false;
          });
        } else {
          this.isLoadingResults = false;
        }
      });

    this.route.params.subscribe((params: Params) => {
      this.id = params['id'];
      this.edicion = params['id'] != null;
      this.initForm();
    });

  }

  initForm() {
    if (this.edicion) {
      this.signoService.listarPorId(this.id).subscribe(data => {
        this.signoForm = this.formBuilder.group({
          idSigno: [data.idSigno],
          paciente: new FormControl(data.paciente),
          fecha: [data.fecha],
          temperatura: [data.temperatura],
          pulso: [data.pulso],
          ritmoRespiratorio: [data.ritmoRespiratorio]
        });
      });
    }
  }

  openDialog() {
    this.dialog.open(PacienteDialogComponent, {
      width: '400px'
    }).afterClosed().subscribe(resp => {
      if (resp) {
        this.signoForm.get('paciente').setValue(resp.data);
      }
    });
  }

  mostrarPaciente(val: Paciente) {
    return val ? `${val.nombres} ${val.apellidos}` : val;
  }

  onFormSubmit() {
    this.isLoadingResults = true;
    if (this.edicion) {
      this.signoService.modificar(this.signoForm.value).pipe(switchMap(() => this.signoService.listar()))
        .subscribe((data) => {
          this.signoService.signoCambio.next(data);
          this.signoService.mensajeCambio.next('SE MODIFICO');
          this.isLoadingResults = false;
        });
    } else {
      this.signoService.registrar(this.signoForm.value).pipe(switchMap(() => this.signoService.listar()))
        .subscribe((data) => {
          this.signoService.signoCambio.next(data);
          this.signoService.mensajeCambio.next('SE REGISTRO');
          this.isLoadingResults = false;
        });
    }
    this.router.navigate(['signo']);
  }

}
