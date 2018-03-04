import {Component, OnInit} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Table, Trigger} from "../../models/rest-models";
import {DatabaseService} from "../../services/database.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {isNullOrUndefined} from "util";
import {CreateTableComponent} from "../table/create-table/create-table.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {HeaderElement} from "../../models/header-element";
import {AlterTableComponent} from "../table/alter-table/alter-table.component";
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

    tables$: Promise<Table[]>;
    views$: Promise<Table[]>;
    triggers$: Promise<Trigger[]>;

    constructor(private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private route: ActivatedRoute,
                private router: Router,
                private modalService: NgbModal)
    {
    }

    ngOnInit()
    {
        this.route.queryParams.subscribe(params =>
        {
            if (isNullOrUndefined(params['schema']))
            {
                this.router.navigate(['/not-found']);
                return;
            }

            if (this.schema != params['schema'] && !isNullOrUndefined(params['schema']))
            {
                this.schema = params['schema'];
                this.tables$ = this.databaseService.getAllTables(this.schema);
                this.tables$
                    .catch(promise =>
                    {
                        this.router.navigate(["/error"], {replaceUrl: true,
                            queryParams: {code: promise.status, message: promise.statusText}});
                    });
                this.views$ = this.databaseService.getAllTables(this.schema, true);
                this.views$
                    .catch(promise =>
                    {
                        this.router.navigate(["/error"], {replaceUrl: true,
                            queryParams: {code: promise.status, message: promise.statusText}});
                    });
                this.triggers$ = this.databaseService.getAllTriggers(this.schema);
                this.triggers$
                    .catch(promise =>
                    {
                        this.router.navigate(["/error"], {queryParams: {code: promise.status, message: promise.statusText}});
                    });

                this.pageHeaderService.addFragment('dbhome', this.pageHeaderService.getHeaderByID('home'),
                    this.router.url, this.schema, 'fa-database');
            }
        });
    }

    dropTable(table: Table)
    {
        this.databaseService.dropTable(table.schema, table.name)
            .then(() =>
            {
                this.tables$ = this.databaseService.getAllTables(this.schema);
                this.tables$
                    .catch(promise =>
                    {
                        this.router.navigate(["/error"], {replaceUrl: true,
                            queryParams: {code: promise.status, message: promise.statusText}});
                    });
            })
            .catch(promise =>
            {
                this.router.navigate(["/error"], {queryParams: {code: promise.status, message: promise.statusText}});
            });
    }

    dropTrigger(trigger: Trigger)
    {
        this.databaseService.dropTrigger(trigger.schema, trigger.name)
            .then(() =>
            {
                this.triggers$ = this.databaseService.getAllTriggers(this.schema);
                this.triggers$
                    .catch(promise =>
                    {
                        this.router.navigate(["/error"], {queryParams: {code: promise.status, message: promise.statusText}});
                    });
            })
            .catch(promise =>
            {
                this.router.navigate(["/error"], {queryParams: {code: promise.status, message: promise.statusText}});
            });
    }

    showTriggerCode(template)
    {
        this.modalService.open(template);
    }
}
