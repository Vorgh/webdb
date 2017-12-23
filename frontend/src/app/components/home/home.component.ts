import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Table} from "../../models/table";
import {DatabaseService} from "../../services/database.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit
{
  title = 'Tables';
  tableList: Table[] = [];

  constructor(private http: HttpClient,
              private databaseService: DatabaseService)
  {
  }

  ngOnInit(): void
  {
    this.databaseService.getAllTables()
      .then(tables => this.tableList = tables)
      .catch(error => console.log(error));
  }
}
