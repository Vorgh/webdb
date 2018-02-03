import {Component, OnInit} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Column} from "../../models/rest-models";
import {DatabaseService} from "../../services/database.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {isNullOrUndefined} from "util";
import {Table} from "../../models/rest-models";
import {HeaderElement} from "../../models/header-element";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";

@Component({
    selector: 'app-table',
    templateUrl: './table.component.html',
    styleUrls: ['./table.component.scss'],
    animations: [routerTransition()]
})
export class TableComponent implements OnInit
{
    header: HeaderElement;

    columns: Column[];
    rows: any[];
    metadata: Table;
    schema: string;
    table: string;

    constructor(private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private route: ActivatedRoute,
                private router: Router)
    {
    }

    ngOnInit()
    {
        this.route.queryParams.subscribe(params =>
        {
            this.schema = params['schema'];
        });

        this.route.params.subscribe((params: Params) =>
        {
            if (isNullOrUndefined(this.schema))
            {
                this.router.navigate(['/home']);
            }

            if (this.table != params['table'] && !isNullOrUndefined(params['table']))
            {
                this.table = params['table'];
                this.databaseService.getColumns(this.schema, this.table)
                    .then(columns => this.columns = columns)
                    .catch(error => console.log(error));

                this.databaseService.getRows(this.schema, this.table)
                    .then(rows => this.rows = rows)
                    .catch(error => console.log(error));

                this.databaseService.getTable(this.schema, this.table)
                    .then(metadata => this.metadata = metadata)
                    .catch(error => console.log(error));

                this.header = <HeaderElement>{
                    id: 'table',
                    parent: this.pageHeaderService.getHeaderByID('dbhome'),
                    link: this.router.url,
                    title: this.table,
                    icon: 'fa-table'
                };
                this.pageHeaderService.addFragment(this.header);
            }
        });
    }
}
