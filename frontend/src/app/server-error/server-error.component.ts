import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-server-error',
    templateUrl: './server-error.component.html',
    styleUrls: ['./server-error.component.scss']
})
export class ServerErrorComponent implements OnInit
{
    code: string;
    message: string;

    constructor(private route: ActivatedRoute)
    {
    }

    ngOnInit()
    {
        this.route.queryParams.subscribe(params =>
        {
            this.code = params.code;
            this.message = params.message
        });
    }

}
