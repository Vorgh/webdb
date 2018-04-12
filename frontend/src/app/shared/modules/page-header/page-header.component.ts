import {Component, Input} from '@angular/core';
import {HeaderElement} from "../../../models/header-element";
import {DropDbComponent} from "../../../layout/components/db-dialog/drop-db/drop-db.component";
import {Router} from "@angular/router";
import {DatabaseService} from "../../../services/database.service";
import {DbModalService} from "../../../services/db-modal.service";
import {GlobalErrorHandler} from "../../../services/error-handler.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-page-header',
    templateUrl: './page-header.component.html',
    styleUrls: ['./page-header.component.scss']
})
export class PageHeaderComponent
{
    @Input() path: HeaderElement[];
    @Input() showDropButton: boolean = false;
    @Input() dropButtonSchema: string;

    constructor(private databaseService: DatabaseService,
                private router: Router,
                private modalService: NgbModal,
                private errorHandler: GlobalErrorHandler,
                private dbModalService: DbModalService)
    {
    }

    dropDatabase()
    {
        const modalRef = this.modalService.open(DropDbComponent, {centered: true, size: "lg"});
        modalRef.componentInstance.databaseName = this.dropButtonSchema;

        modalRef.result
                .then(() =>
                {
                    this.databaseService.dropSchema(this.dropButtonSchema)
                        .then(() =>
                        {
                            this.dbModalService.refreshSidebar();
                            this.router.navigate(['/home'])
                        })
                        .catch(error => this.errorHandler.handleError(error));
                })
                .catch(() => null);
    }
}
