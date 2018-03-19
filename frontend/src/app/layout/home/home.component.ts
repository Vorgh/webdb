import {Component, OnInit} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Table} from "../../models/rest/rest-models";
import {DatabaseService} from "../../services/database.service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    animations: [routerTransition()]
})
export class HomeComponent implements OnInit
{
    constructor()
    {
    }

    ngOnInit()
    {
    }
}
