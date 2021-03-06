import {Component, OnInit} from '@angular/core';
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {DatabaseService} from "../../services/database.service";
import {Column, Row, View} from "../../models/rest/rest-models";
import {ActivatedRoute, Router} from "@angular/router";
import {isNullOrUndefined} from "util";
import {GlobalErrorHandler} from "../../services/error-handler.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-view',
    templateUrl: './view.component.html',
    styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit
{
    columns: Column[] = [];
    rows: Row[] = [];
    metadata: View;
    schema: string;
    viewName: string;

    pageHeaderPath = [];

    constructor(private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private route: ActivatedRoute,
                private router: Router,
                private errorHandler: GlobalErrorHandler,
                private modalService: NgbModal)
    {
    }

    ngOnInit()
    {
        this.route.queryParams.subscribe(params =>
        {
            if (!isNullOrUndefined(params['schema']) && !isNullOrUndefined(params['view']))
            {
                this.schema = params['schema'];
                this.viewName = params['view'];

                Promise.all([
                    this.databaseService.getColumns(this.schema, this.viewName),
                    this.databaseService.getRows(this.schema, this.viewName),
                    this.databaseService.getView(this.schema, this.viewName)
                ])
                       .then((values: any[]) =>
                       {
                           this.columns = values[0];
                           this.rows = values[1];
                           this.metadata = values[2];

                           this.pageHeaderPath = this.pageHeaderService.getPathFromID('view', null, this.metadata);
                       })
                       .catch(error => this.errorHandler.handleError(error));
            }
            else
            {
                this.errorHandler.notFound();
            }
        });
    }

    showCodeModal(template)
    {
        this.modalService.open(template);
    }
}
