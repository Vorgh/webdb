import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-confirmdialog',
    templateUrl: './confirmdialog.component.html',
    styleUrls: ['./confirmdialog.component.scss']
})
export class ConfirmdialogComponent implements OnInit
{
    @Input() dbObject: string = "this object";
    @Input() type: "create" | "modify" | "delete" = "modify";

    constructor(public activeModal: NgbActiveModal)
    {
    }

    ngOnInit()
    {
    }

}
