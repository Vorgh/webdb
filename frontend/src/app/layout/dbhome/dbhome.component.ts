import {Component, OnInit} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Table} from "../../models/table";
import {DatabaseService} from "../../services/database.service";
import {ActivatedRoute, Params} from "@angular/router";
import {isNullOrUndefined} from "util";

@Component({
    selector: 'app-dbhome',
    templateUrl: './dbhome.component.html',
    styleUrls: ['./dbhome.component.scss'],
    animations: [routerTransition()]
})
export class DBHomeComponent implements OnInit
{
    tables$: Promise<Table[]>;
    schema: string;

    constructor(private databaseService: DatabaseService,
                private route: ActivatedRoute)
    {
    }

    ngOnInit()
    {
        this.route.params.subscribe((params: Params) =>
        {
            if (this.schema != params['schema'] && !isNullOrUndefined(params['schema']))
            {
                this.schema = params['schema'];
                this.tables$ = this.databaseService.getAllTables(this.schema);
            }
        });
    }
}
