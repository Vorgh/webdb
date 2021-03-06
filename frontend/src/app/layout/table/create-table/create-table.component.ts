import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Column, Constraint, Table} from "../../../models/rest/rest-models";
import {isNullOrUndefined} from "util";
import {Utils} from "../../../shared/util/utils";
import {DatabaseService} from "../../../services/database.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PageHeaderService} from "../../../shared/modules/page-header/page-header.service";
import {newColumnValidator, newForeignKeyValidator} from "../../../shared/validator/add-new.validator";
import {columnTypeValidator} from "../../../shared/validator/column-type.validator";
import {GlobalErrorHandler} from "../../../services/error-handler.service";
import {ConfirmdialogComponent} from "../../components/confirmdialog/confirmdialog.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'create-table',
    templateUrl: './create-table.component.html',
    styleUrls: ['./create-table.component.scss']
})
export class CreateTableComponent implements OnInit
{
    readonly CASCADE_OPTIONS = ["RESTRICT", "CASCADE", "SET NULL", "NO ACTION"];

    schema: string;
    createTableForm: FormGroup;
    types = Utils.dataTypes;
    newColumnValidateMessage: string;
    newForeignKeyValidateMessage: string;

    pageHeaderPath = [];

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

                this.pageHeaderPath = this.pageHeaderService.getPathFromID('create-table', this.schema);
            }
            else
            {
                this.errorHandler.notFound();
            }
        });

        this.createTableForm = this.formBuilder.group({
                tableName: ['', Validators.required],
                columns: this.formBuilder.array([]),
                constraints: this.formBuilder.array([])
            }
        );
    }

    get formColumns(): FormArray
    {
        return this.createTableForm.get('columns') as FormArray;
    }

    get formConstraints(): FormArray
    {
        return this.createTableForm.get('constraints') as FormArray;
    }

    addColumn(name, type, defaultValue, PK, UQ, NUL, AI)
    {
        let col = new Column();

        this.newColumnValidateMessage = newColumnValidator(name, type);
        if (this.newColumnValidateMessage != null)
        {
            return;
        }

        col.tableSchema = this.schema;
        col.name = name.value;
        col.columnType = type.value;
        col.defaultValue = defaultValue.value;
        col.primaryKey = PK.checked;
        col.unique = UQ.checked;
        col.nullable = NUL.checked;
        col.autoIncrement = AI.checked;

        this.addColumnControl(col);

        name.value = '';
        type.value = '';
        defaultValue.value = '';
        PK.checked = false;
        UQ.checked = false;
        NUL.checked = true;
        AI.checked = false;
    }

    private addColumnControl(col: Column)
    {
        let group: FormGroup = this.formBuilder.group(col);
        for (let key of Object.keys(group.value))
        {
            let control = group.get(key);
            switch (key)
            {
                case "name":
                    control.setValidators(Validators.required);
                    break;
                case "columnType":
                    control.setValidators([Validators.required, columnTypeValidator()]);
                    break;
            }
        }

        this.formColumns.push(group);
    }

    addForeignKey(name, column, reference, updateRule, deleteRule)
    {
        let key: Constraint = new Constraint();

        this.newForeignKeyValidateMessage = newForeignKeyValidator(name, column, reference, updateRule, deleteRule);
        if (this.newForeignKeyValidateMessage != null)
        {
            return;
        }

        let parsedRef = Utils.parseColumnReference(reference.value);
        key.constraintName = name.value;
        key.schema = this.schema;
        key.column = column.value;
        key.refSchema = parsedRef[0];
        key.refTable = parsedRef[1];
        key.refColumn = parsedRef[2];
        key.updateRule = updateRule.value;
        key.deleteRule = deleteRule.value;

        this.formConstraints.push(this.formBuilder.group(key));

        name.value = '';
        column.value = '';
        reference.value = '';
        updateRule.value = this.CASCADE_OPTIONS[0];
        deleteRule.value = this.CASCADE_OPTIONS[0];
    }

    removeColumn(index: number)
    {
        this.formColumns.removeAt(index);
    }

    removeForeignKey(index: number)
    {
        this.formColumns.removeAt(index);
    }

    submit()
    {
        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = this.createTableForm.get('tableName').value;
        modalRef.componentInstance.type = "create";

        modalRef.result
            .then(() =>
            {
                this.databaseService.createTable(this.schema, this.createTableForm.get('tableName').value,
                    this.formColumns.value, this.formConstraints.value)
                    .then(() => this.router.navigate(['/db'], {queryParams: {schema: this.schema, tab: 'table'}}))
                    .catch(error => this.errorHandler.handleError(error));
            })
            .catch(() => null);
    }

    concatSchemaTableColumn(schema: string, table: string, column: string): string
    {
        return `${schema}.${table}(${column})`;
    }
}
