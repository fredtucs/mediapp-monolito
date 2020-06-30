import { Component, OnInit, Inject } from '@angular/core';
import { Paciente } from 'src/app/_model/paciente';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PacienteService } from 'src/app/_service/paciente.service';
import { FormGroup, FormControl, FormGroupDirective, NgForm, Validators, FormBuilder } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { switchMap } from 'rxjs/operators';
import { SignoService } from 'src/app/_service/signo.service';

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-paciente-dialog',
  templateUrl: './paciente-dialog.component.html',
  styleUrls: ['./paciente-dialog.component.css']
})
export class PacienteDialogComponent implements OnInit {

  pacienteForm: FormGroup;

  matcher = new MyErrorStateMatcher();

  constructor(
    private pacienteService: PacienteService,
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<PacienteDialogComponent>,
    private signoService: SignoService,
  ) { }

  ngOnInit(): void {
    this.pacienteForm = this.formBuilder.group({
      nombres: [null, [Validators.required, Validators.minLength(3)]],
      apellidos: [null, Validators.required],
      dni: [null, Validators.required],
      telefono: [null, Validators.required],
      direccion: [null, Validators.required]
    });
  }

  onFormSubmit() {
    this.pacienteService.registrar(this.pacienteForm.value)
      .subscribe((dataPaciente) => {
        this.signoService.mensajeCambio.next('SE REGISTRO PACIENTE');
        this.dialogRef.close({ data: dataPaciente });
      });
  }

  cancelar() {
    this.dialogRef.close();
  }

}
