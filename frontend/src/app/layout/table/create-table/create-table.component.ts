import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {Column, Constraint, Table} from "../../../models/rest-models";
import {isNullOrUndefined} from "util";
import {Utils} from "../../../shared/util/utils";
import {DatabaseService} from "../../../services/database.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PageHeaderService} from "../../../shared/modules/page-header/page-header.service";

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

    constructor(private formBuilder: FormBuilder,
                private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private router: Router,
                private route: ActivatedRoute)
    {
    }

    ngOnInit()
    {
        this.route.queryParams.subscribe(params =>
        {
            if (!isNullOrUndefined(params['schema']))
            {
                this.schema = params['schema'];

                this.pageHeaderService.addFragment('create-table', this.pageHeaderService.getHeaderByID('dbhome'),
                    this.router.url, 'New Table', 'fa-table');
            }
            else
            {
                this.router.navigate(['/not-found']);
            }
        });

        this.createTableForm = this.formBuilder.group({
                tableName: [''],
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
        col.tableSchema = this.schema;
        col.name = name.value;
        col.columnType = type.value;
        col.defaultValue = defaultValue.value;
        col.primaryKey = PK.checked;
        col.unique = UQ.checked;
        col.nullable = NUL.checked;
        col.autoIncrement = AI.checked;

        this.formColumns.push(this.formBuilder.group(col));

        name.value = '';
        type.value = '';
        defaultValue.value = '';
        PK.checked = false;
        UQ.checked = false;
        NUL.checked = true;
        AI.checked = false;
    }

    addForeignKey(name, column, reference, updateRule, deleteRule)
    {
        let key: Constraint = new Constraint();
        let parsedRef = Utils.parseColumnReference(reference.value);

        if (isNullOrUndefined(parsedRef)) return;

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
        this.databaseService.createTable(this.schema, this.createTableForm.get('tableName').value,
            this.formColumns.value, this.formConstraints.value)
            .then(() => this.router.navigate(['/db']))
            .catch(error => console.log(error));
    }

    concatSchemaTableColumn(schema: string, table: string, column: string): string
    {
        return `${schema}.${table}(${column})`;
    }
}
