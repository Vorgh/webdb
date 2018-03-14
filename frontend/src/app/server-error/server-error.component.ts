import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-server-error',
    templateUrl: './server-error.component.html',
    styleUrls: ['./server-error.component.scss']
})
export class ServerErrorComponent implements OnInit
{
    code: number;
    message: string;

    constructor(private route: ActivatedRoute, private router: Router)
    {
    }

    ngOnInit()
    {
        this.route.queryParams.subscribe(params =>
        {
            this.code = params.code;
            this.message = params.message;

            if (this.code == 401)
            {
                localStorage.removeItem('access_token');
                localStorage.removeItem('current_user');
                this.router.navigate(['/login']);
            }
        });
    }

}
