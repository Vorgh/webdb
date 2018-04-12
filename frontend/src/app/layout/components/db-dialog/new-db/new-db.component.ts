import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {DbModalService} from "../../../../services/db-modal.service";

@Component({
    selector: 'app-new-db',
    templateUrl: './new-db.component.html',
    styleUrls: ['./new-db.component.scss']
})
export class NewDbComponent
{
    databaseName: string;

    constructor(public activeModal: NgbActiveModal)
    {
    }

    onSubmit()
    {
        this.activeModal.close(this.databaseName);
    }
}
