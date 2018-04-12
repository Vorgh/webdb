import {Component, OnInit} from '@angular/core';
import {GlobalErrorHandler} from "../../../services/error-handler.service";
import {PageHeaderService} from "../../../shared/modules/page-header/page-header.service";
import {ConfirmdialogComponent} from "../../components/confirmdialog/confirmdialog.component";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DatabaseService} from "../../../services/database.service";
import {isNullOrUndefined} from "util";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {View} from "../../../models/rest/rest-models";

@Component({
    selector: 'app-create-view',
    templateUrl: './create-view.component.html',
    styleUrls: ['./create-view.component.scss']
})
export class CreateViewComponent implements OnInit
{
    schema: string;
    createViewForm: FormGroup;

    pageHeaderPath = [];
    editorOptions = {
        theme: 'mysqlTheme',
        language: 'mysql',
        autocomplete: true,
        automaticLayout: true,
        minimap: {enabled: false},
        scrollBeyondLastLine: false,
        fontSize: 16,
        formatOnPaste: true,
        lineNumbers: "off"};

    constructor(private formBuilder: FormBuilder,
                private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private router: Router,
                private route: ActivatedRoute,
                private errorHandler: GlobalErrorHandler,
                private modalService: NgbModal)
    {
    }

    ngOnInit()
    {
        this.route.queryParams.subscribe(params =>
        {
            if (!isNullOrUndefined(params['schema']))
            {
                this.schema = params['schema'];

                this.pageHeaderPath = this.pageHeaderService.getPathFromID('create-view', this.schema);
            }
            else
            {
                this.errorHandler.notFound();
            }
        });

        this.createViewForm = this.formBuilder.group({
                viewName: ['', Validators.required],
                viewSelectQuery: ['', Validators.required],
            }
        );
    }

    submit()
    {
        let request: View = new View();
        request.schema = this.schema;
        request.name = this.createViewForm.get('viewName').value;
        request.selectQuery = this.createViewForm.get('viewSelectQuery').value;

        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = request.name;
        modalRef.componentInstance.type = "create";

        modalRef.result
                .then(() =>
                {
                    this.databaseService.createView(request)
                        .then(() => this.router.navigate(['/db'], { queryParams: {schema: this.schema, tab: 'view'}}))
                        .catch(error => this.errorHandler.handleError(error));
                })
                .catch(() => null);
    }

    getFormControl(name: string): AbstractControl
    {
        return this.createViewForm.get(name);
    }
}
