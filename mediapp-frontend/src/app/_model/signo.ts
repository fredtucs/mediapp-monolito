import { Paciente } from './paciente';

export class Signo {
  idSigno: number;
  paciente: Paciente;
  fecha: Date;
  temperatura: number;
  pulso: number;
  ritmoRespiratorio: string;
}
