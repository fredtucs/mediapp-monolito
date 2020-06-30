import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {

  username: any;
  roles: any;

  constructor() { }

  ngOnInit(): void {
    const helper = new JwtHelperService();
    const token = sessionStorage.getItem(environment.TOKEN_NAME);
    const decodedToken = helper.decodeToken(token);
    this.username = decodedToken.user_name;
    this.roles = decodedToken.authorities;
  }

}
