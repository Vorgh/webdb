import {Component, OnInit} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Table} from "../../models/table";
import {Observable} from "rxjs/Observable";
import {DatabaseService} from "../../services/database.service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    animations: [routerTransition()]
})
export class HomeComponent implements OnInit
{
    tables$: Promise<Table[]>;

    constructor(private databaseService: DatabaseService)
    {
    }

    ngOnInit()
    {
        this.databaseService.getCurrentSchemaObservable().subscribe(schema =>
        {
            this.tables$ = this.databaseService.getAllTables(schema);
        });
    }
}
