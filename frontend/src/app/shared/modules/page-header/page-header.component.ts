import {Component, OnInit, Input, OnChanges} from '@angular/core';
import {HeaderElement} from "../../../models/header-element";
import {PageHeaderService} from "./page-header.service";
import {isNullOrUndefined} from "util";

@Component({
    selector: 'app-page-header',
    templateUrl: './page-header.component.html',
    styleUrls: ['./page-header.component.scss']
})
export class PageHeaderComponent implements OnChanges
{
    @Input() header: HeaderElement;
    path: HeaderElement[];

    constructor(private pageHeaderService: PageHeaderService)
    {
    }

    ngOnChanges()
    {
        if (!isNullOrUndefined(this.header))
        {
            console.log(this.header);
            this.path = this.pageHeaderService.getPathFromHeader(this.header);
        }
    }
}
