import {Component, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Procedure, Table, Trigger} from "../../models/rest/rest-models";
import {DatabaseService} from "../../services/database.service";
import {ActivatedRoute, Router} from "@angular/router";
import {isNullOrUndefined} from "util";
import {NgbModal, NgbTab, NgbTabChangeEvent, NgbTabset} from "@ng-bootstrap/ng-bootstrap";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {GlobalErrorHandler} from "../../services/error-handler.service";
import {DbDataWrapper} from "../../models/rest/db-data-wrapper";
import {ConfirmdialogComponent} from "../components/confirmdialog/confirmdialog.component";

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
    tables: Table[];
    triggers: Trigger[];
    procedures: Procedure[];

    pageHeaderPath = [];

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
        this.route.data.subscribe((data: {dbData: DbDataWrapper}) =>
        {
            let dbData = data.dbData;

            this.schema = dbData.schema;
            this.tables = dbData.tables;
            this.triggers = dbData.triggers;
            this.procedures = dbData.procedures;

            this.pageHeaderPath = this.pageHeaderService.getPathFromID('dbhome', this.schema);
        });

        this.route.queryParams.subscribe(params =>
        {
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
        });
    }

    dropTable(table: Table)
    {
        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = table.name;
        modalRef.componentInstance.type = "delete";

        modalRef.result
                .then(() =>
                {
                    this.databaseService.dropTable(table.schema, table.name)
                        .then(() =>
                        {
                            return this.databaseService.getAllTables(this.schema)
                                       .then(tables => this.tables = tables)
                                       .catch(error => this.errorHandler.handleError(error));
                        })
                        .catch(error => this.errorHandler.handleError(error));
                })
                .catch(() => null);
    }

    dropView(view: Table)
    {
        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = view.name;
        modalRef.componentInstance.type = "delete";

        modalRef.result
                .then(() =>
                {
                    this.databaseService.dropView(view.schema, view.name)
                        .then(() =>
                        {
                            return this.databaseService.getAllTables(this.schema)
                                       .then(tables => this.tables = tables)
                                       .catch(error => this.errorHandler.handleError(error));
                        })
                        .catch(error => this.errorHandler.handleError(error));
                })
                .catch(() => null);
    }

    dropTrigger(trigger: Trigger)
    {
        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = trigger.name;
        modalRef.componentInstance.type = "delete";

        modalRef.result
                .then(() =>
                {
                    this.databaseService.dropTrigger(trigger.schema, trigger.name)
                        .then(() =>
                        {
                            return this.databaseService.getAllTriggers(this.schema)
                                       .then(triggers => this.triggers = triggers)
                                       .catch(error => this.errorHandler.handleError(error))
                        })
                        .catch(error => this.errorHandler.handleError(error));
                })
                .catch(() => null);
    }

    dropProcedure(procedure: Procedure, isFunction: string = "false")
    {
        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = procedure.name;
        modalRef.componentInstance.type = "delete";

        modalRef.result
                .then(() =>
                {
                    this.databaseService.dropProcedure(procedure.schema, procedure.name, isFunction)
                        .then(() =>
                        {
                            return this.databaseService.getAllProcedures(this.schema)
                                       .then(procedures => this.procedures = procedures)
                                       .catch(error => this.errorHandler.handleError(error))

                        })
                        .catch(error => this.errorHandler.handleError(error));
                })
                .catch(() => null);
    }

    showCodeModal(template)
    {
        this.modalService.open(template);
    }

    beforeTabChange(event: NgbTabChangeEvent)
    {
        if (event.nextId === 'tab-custom') {
            event.preventDefault();
            this.router.navigate(['/custom'])
        }
    }
}
