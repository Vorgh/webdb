import {Component, OnInit} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {CookieService} from "ngx-cookie-service";
import {NewDbComponent} from "../components/db-dialog/new-db/new-db.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DatabaseService} from "../../services/database.service";
import {GlobalErrorHandler} from "../../services/error-handler.service";
import {DbModalService} from "../../services/db-modal.service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    animations: [routerTransition()]
})
export class HomeComponent implements OnInit
{
    user: string;

    constructor(private cookieService: CookieService,
                private databaseService: DatabaseService,
                private errorHandler: GlobalErrorHandler,
                private modalService: NgbModal,
                private dbModalService: DbModalService)
    {
    }

    ngOnInit()
    {
        this.user = this.cookieService.get("user");
    }

    newDatabase(event)
    {
        event.preventDefault();
        const modalRef = this.modalService.open(NewDbComponent, {size: "lg", centered: true});

        modalRef.result
            .then((newDbName) =>
            {
                this.databaseService.createSchema(newDbName)
                    .then(() =>
                    {
                        this.dbModalService.refreshSidebar();
                    })
                    .catch(error => this.errorHandler.handleError(error));
            })
            .catch(() => null);
    }
}
