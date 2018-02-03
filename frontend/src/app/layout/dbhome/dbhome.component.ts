import {Component, OnInit} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Table} from "../../models/rest-models";
import {DatabaseService} from "../../services/database.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {isNullOrUndefined} from "util";
import {CreateTableComponent} from "../components/create-table/create-table.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {HeaderElement} from "../../models/header-element";
import {AlterTableComponent} from "../components/alter-table/alter-table.component";
import {Column} from "../../models/rest-models";

@Component({
    selector: 'app-dbhome',
    templateUrl: './dbhome.component.html',
    styleUrls: ['./dbhome.component.scss'],
    animations: [routerTransition()]
})
export class DBHomeComponent implements OnInit
{
    schema: string;
    tables: Table[];
    columns: Column[];

    tables$: Promise<Table[]>;

    constructor(private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private route: ActivatedRoute,
                private router: Router,
                private modalService: NgbModal)
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
                this.tables$.then(tables => this.tables = tables);

                this.pageHeaderService.addFragment(<HeaderElement>{
                    id: 'dbhome',
                    parent: this.pageHeaderService.getHeaderByID('home'),
                    link: this.router.url,
                    title: this.schema,
                    icon: 'fa-database'
                });
            }
        });
    }

    openTableModal()
    {
        const modalRef = this.modalService.open(CreateTableComponent, {size: "lg", backdrop: "static"});
        modalRef.componentInstance.tableList = this.tables;

    }

    dropTable(table: Table)
    {
        //TODO
    }

    alterTable(table: Table)
    {
        Promise.all([
            this.databaseService.getColumns(this.schema, table.name),
            this.databaseService.getForeignKeys(this.schema, table.name),
            this.databaseService.getIndexes(this.schema, table.name)
        ])
               .then(result =>
               {
                   const modalRef = this.modalService.open(AlterTableComponent, {size: "lg", backdrop: "static"});
                   modalRef.componentInstance.table = table;
                   modalRef.componentInstance.columnList = result[0];
                   modalRef.componentInstance.foreignKeys = result[1];
                   modalRef.componentInstance.indexList = result[2];
               })
               .catch(error => console.log(error));
    }
}
