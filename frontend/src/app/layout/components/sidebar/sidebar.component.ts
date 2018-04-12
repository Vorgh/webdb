import {Component, OnInit} from '@angular/core';
import {Router, NavigationEnd} from '@angular/router';
import {DatabaseService} from "../../../services/database.service";
import {Schema} from "../../../models/rest/rest-models";
import {ConnectionService} from "../../../services/connection.service";
import {GlobalErrorHandler} from "../../../services/error-handler.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewDbComponent} from "../db-dialog/new-db/new-db.component";
import {DbModalService} from "../../../services/db-modal.service";

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit
{
    isActive: boolean = false;
    pushRightClass: string = 'push-right';
    schemas: Schema[];

    constructor(private databaseService: DatabaseService,
                private connectionService: ConnectionService,
                private router: Router,
                private errorHandler: GlobalErrorHandler,
                private modalService: NgbModal,
                private dbModalService: DbModalService)
    {
        this.router.events.subscribe(val =>
        {
            if (
                val instanceof NavigationEnd &&
                window.innerWidth <= 992 &&
                this.isToggled()
            )
            {
                this.toggleSidebar();
            }
        });
    }

    ngOnInit()
    {
        this.refreshSchemaList();

        this.dbModalService.getRefreshObservable().subscribe(() =>
        {
            this.refreshSchemaList();
        })
    }


    isToggled(): boolean
    {
        const dom: Element = document.querySelector('body');
        return dom.classList.contains(this.pushRightClass);
    }

    toggleSidebar()
    {
        const dom: any = document.querySelector('body');
        dom.classList.toggle(this.pushRightClass);
    }

    onLoggedout()
    {
        this.connectionService.logout();
    }

    newDatabase()
    {
        const modalRef = this.modalService.open(NewDbComponent);

        modalRef.result
            .then((newDbName) =>
            {
                this.databaseService.createSchema(newDbName)
                    .then(() =>
                    {
                        this.refreshSchemaList();
                    })
                    .catch(error => this.errorHandler.handleError(error));
            })
            .catch(() => null);
    }

    private refreshSchemaList()
    {
        this.databaseService.getAllSchemas()
            .then(schemas => this.schemas = schemas)
            .catch(error => this.errorHandler.handleError(error));
    }
}
