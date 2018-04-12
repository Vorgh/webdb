import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-drop-db',
    templateUrl: './drop-db.component.html',
    styleUrls: ['./drop-db.component.scss']
})
export class DropDbComponent
{
    databaseName: string;
    confirmationText: string;
    isConfirmValid: boolean;

    constructor(public activeModal: NgbActiveModal)
    {
    }

    onSubmit()
    {
        if (this.isConfirmValid)
        {
            this.activeModal.close();
        }
    }

    onChange(value)
    {
        this.isConfirmValid = (value == "DROP " + this.databaseName.toUpperCase());
    }
}
