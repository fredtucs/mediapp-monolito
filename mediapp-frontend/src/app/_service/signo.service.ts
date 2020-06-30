import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../environments/environment';
import { Signo } from './../_model/signo';
import { GenericService } from './generic.service';
import { catchError, retry } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class SignoService extends GenericService<Signo> {

  signoCambio = new Subject<Signo[]>();
  mensajeCambio = new Subject<string>();

  constructor(protected http: HttpClient) {
    super(http, `${environment.HOST}/signos`);
  }

  listarPageable(p: number, s: number) {
    return this.http.get<any>(`${this.url}/pageable?page=${p}&size=${s}`);
  }

  buscarPaciente(key: string) {
    return this.http.get(`${this.url}/paciente/${key}`).pipe(retry(3), catchError(this.errFunc));
  }

  errFunc(err: Response) {
    return Observable.throw(err.statusText);
  }

}
