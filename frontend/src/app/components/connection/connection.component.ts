import {Component, OnInit} from '@angular/core';
import {ConnectionService} from "../../services/connection.service";
import {ConnectionAuthInfo, OAuthTokenResponse} from "../../models/connection";
import {Router} from "@angular/router";

@Component({
  selector: 'app-connection',
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.css']
})
export class ConnectionComponent implements OnInit
{
  connAuth: ConnectionAuthInfo = new ConnectionAuthInfo();

  constructor(private connectionService: ConnectionService,
              private router: Router)
  {
  }

  ngOnInit()
  {

  }

  connectionAuth()
  {
    this.connectionService.connect(this.connAuth)
      .then((response: OAuthTokenResponse) =>
      {
        localStorage.setItem("access_token", response.access_token);
        this.router.navigate(['/home'])
      })
      .catch(error => console.log(error));
  }

}
