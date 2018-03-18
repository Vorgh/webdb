import {AfterViewInit, Component, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Procedure, Table, Trigger} from "../../models/rest-models";
import {DatabaseService} from "../../services/database.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {isNullOrUndefined} from "util";
import {NgbModal, NgbTab, NgbTabset} from "@ng-bootstrap/ng-bootstrap";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {GlobalErrorHandler} from "../../shared/error-handler/error-handler.service";

@Component({
    selector: 'app-dbhome',
    templateUrl: './dbhome.component.html',
    styleUrls: ['./dbhome.component.scss'],
    animations: [routerTransition()]
})
export class DBHomeComponent implements OnInit
{
    @ViewChild(NgbTabset) tabset: NgbTabset;
    @ViewChildren(NgbTab) tabs: QueryList<NgbTab>;
    activeTabId: string;

    schema: string;
    tables$: Promise<Table[]>;
    views$: Promise<Table[]>;
    triggers$: Promise<Trigger[]>;
    procedures$: Promise<Procedure[]>;

    constructor(private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private route: ActivatedRoute,
                private router: Router,
                private modalService: NgbModal,
                private errorHandler: GlobalErrorHandler)
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
                    .catch(this.errorHandler.handleError);

                this.views$ = this.databaseService.getAllTables(this.schema, true);
                this.views$
                    .catch(this.errorHandler.handleError);

                this.triggers$ = this.databaseService.getAllTriggers(this.schema);
                this.triggers$
                    .catch(this.errorHandler.handleError);

                this.procedures$ = this.databaseService.getAllProcedures(this.schema);
                this.procedures$
                    .catch(this.errorHandler.handleError);

                if (!isNullOrUndefined(params['tab']))
                {
                    switch (params['tab'])
                    {
                        case 'table': this.activeTabId = 'tab-tables'; break;
                        case 'view': this.activeTabId = 'tab-views'; break;
                        case 'trigger': this.activeTabId = 'tab-triggers'; break;
                        case 'procedure': this.activeTabId = 'tab-procedures'; break;
                        case 'function': this.activeTabId = 'tab-functions'; break;
                    }
                }

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
                    .catch(this.errorHandler.handleError);
            })
            .catch(this.errorHandler.handleError);
    }

    dropTrigger(trigger: Trigger)
    {
        this.databaseService.dropTrigger(trigger.schema, trigger.name)
            .then(() =>
            {
                this.triggers$ = this.databaseService.getAllTriggers(this.schema);
                this.triggers$
                    .catch(this.errorHandler.handleError);
            })
            .catch(this.errorHandler.handleError);
    }

    dropProcedure(procedure: Procedure)
    {
        this.databaseService.dropProcedure(procedure.schema, procedure.name)
            .then(() =>
            {
                this.procedures$ = this.databaseService.getAllProcedures(this.schema);
                this.procedures$
                    .catch(this.errorHandler.handleError);
            })
            .catch(this.errorHandler.handleError);
    }

    showCodeModal(template)
    {
        this.modalService.open(template);
    }
}
