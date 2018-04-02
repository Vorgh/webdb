import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DatabaseService} from "../../../services/database.service";
import {ConfirmdialogComponent} from "../../components/confirmdialog/confirmdialog.component";
import {PageHeaderService} from "../../../shared/modules/page-header/page-header.service";
import {View} from "../../../models/rest/rest-models";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalErrorHandler} from "../../../services/error-handler.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ModifyRequest} from "../../../models/request/request-models";

@Component({
  selector: 'app-alter-view',
  templateUrl: './alter-view.component.html',
  styleUrls: ['./alter-view.component.scss']
})
export class AlterViewComponent implements OnInit
{
    originalView: View;
    alterViewForm: FormGroup;

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
        this.route.data.subscribe((data: {view: View}) =>
        {
            this.originalView = data.view;

            this.alterViewForm = this.formBuilder.group({
                viewName: [this.originalView.name, Validators.required],
                viewSelectQuery: [this.originalView.selectQuery, Validators.required],
            });

            this.pageHeaderPath = this.pageHeaderService.getPathFromID('modify-view', null, this.originalView);
        });
    }

    submit()
    {
        let newView: View = new View();
        newView.schema = this.originalView.schema;
        newView.name = this.alterViewForm.get('viewName').value;
        newView.selectQuery = this.alterViewForm.get('viewSelectQuery').value;

        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = newView.name;
        modalRef.componentInstance.type = "modify";

        modalRef.result.then(() =>
        {
            let request: ModifyRequest<View> = new ModifyRequest<View>(this.originalView, newView);
            this.databaseService.alterView(request)
                .then(() => this.router.navigate(['/db'], { queryParams: {schema: this.originalView.schema, tab: 'view'}}))
                .catch(error => this.errorHandler.handleError(error));
        });
    }

    getFormControl(name: string): AbstractControl
    {
        return this.alterViewForm.get(name);
    }
}
