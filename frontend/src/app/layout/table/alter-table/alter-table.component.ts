import {Component, OnInit} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Column, Index} from "../../../models/rest/rest-models";
import {Table} from "../../../models/rest/rest-models";
import {Constraint} from "../../../models/rest/rest-models";
import {DatabaseService} from "../../../services/database.service";
import {Utils} from "../../../shared/util/utils";
import {ActivatedRoute, Router} from "@angular/router";
import {PageHeaderService} from "../../../shared/modules/page-header/page-header.service";
import {ConfirmdialogComponent} from "../../components/confirmdialog/confirmdialog.component";
import {columnTypeValidator} from "../../../shared/validator/column-type.validator";
import {newColumnValidator, newForeignKeyValidator, newIndexValidator} from "../../../shared/validator/add-new.validator";
import {GlobalErrorHandler} from "../../../services/error-handler.service";

@Component({
    selector: 'alter-table',
    templateUrl: './alter-table.component.html',
    styleUrls: ['./alter-table.component.scss']
})
export class AlterTableComponent implements OnInit
{
    readonly CASCADE_OPTIONS = ["RESTRICT", "CASCADE", "SET NULL", "NO ACTION"];

    schemaName: string;
    table: Table;
    columnList: Column[] = [];
    foreignKeys: Constraint[] = [];
    indexList: Index[] = [];

    alterTableForm: FormGroup;
    originalForm: FormGroup;
    changes: any;
    types = Utils.dataTypes;

    newColumnValidateMessage: string;
    newForeignKeyValidateMessage: string;
    newIndexValidateMessage: string;

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
        this.route.data.subscribe((data: {table: Table}) =>
        {
            this.table = data.table;
            this.schemaName = data.table.schema;

            this.pageHeaderPath = this.pageHeaderService.getPathFromID('modify-table', null, this.table);

            Promise.all([
                this.databaseService.getColumns(this.schemaName, this.table.name),
                this.databaseService.getForeignKeys(this.schemaName, this.table.name),
                this.databaseService.getIndexes(this.schemaName, this.table.name)])
                   .then(result =>
                   {
                       this.columnList = result[0];
                       this.foreignKeys = result[1];
                       this.indexList = result[2];

                       this.initFields();
                       this.alterTableForm.valueChanges.subscribe(change =>
                       {
                           this.handleChange(change);
                       });
                   })
                   .catch(error => this.errorHandler.handleError(error));
        });

        this.originalForm = this.formBuilder.group({
                tableName: [this.table.name],
                columns: this.formBuilder.array([]),
                constraints: this.formBuilder.array([]),
                indexes: this.formBuilder.array([])
            }
        );
        this.alterTableForm = this.formBuilder.group({
                tableName: [this.table.name, Validators.required],
                columns: this.formBuilder.array([]),
                constraints: this.formBuilder.array([]),
                indexes: this.formBuilder.array([])
            }
        );
    }

    private handleChange(change: any)
    {
        if (this.alterTableForm.valid)
        {
            let changeObj: any = {};
            if (this.originalForm.get('tableName').value !== change['tableName'])
            {
                changeObj.nameChange = change['tableName'];
            }

            changeObj.columnChange = [];
            for (let key of Object.keys(this.formColumns.value))
            {
                /*if (this.formColumns.value[key].name === "" || this.formColumns.value[key].columnType === "")
                    continue;*/

                if (this.formColumns.value[key].deleted)
                {
                    changeObj.columnChange.push(
                        {
                            from: this.originalFormColumns.value[key],
                            to: null
                        }
                    );
                    continue;
                }

                if (this.formColumns.value[key].added)
                {
                    changeObj.columnChange.push(
                        {
                            from: null,
                            to: this.formColumns.value[key]
                        }
                    );
                    continue;
                }

                for (let key2 of Object.keys(this.formColumns.value[key]))
                {
                    if (this.originalFormColumns.value[key][key2] !== this.formColumns.value[key][key2])
                    {
                        changeObj.columnChange.push(
                            {
                                from: this.originalFormColumns.value[key],
                                to: this.formColumns.value[key]
                            });
                        break;
                    }
                }
            }

            changeObj.constraintChange = [];
            for (let key of Object.keys(this.formConstraints.value))
            {
                if (this.formConstraints.value[key].deleted)
                {
                    changeObj.constraintChange.push(
                        {
                            from: this.originalFormConstraints.value[key],
                            to: null
                        });
                    continue;
                }

                if (this.formConstraints.value[key].added)
                {
                    changeObj.constraintChange.push(
                        {
                            from: null,
                            to: this.formConstraints.value[key]
                        }
                    );
                }
            }

            changeObj.indexChange = [];
            for (let key of Object.keys(this.formIndexes.value))
            {
                if (this.formIndexes.value[key].deleted)
                {
                    changeObj.indexChange.push(
                        {
                            from: this.originalFormIndexes.value[key],
                            to: null
                        });
                    continue;
                }

                if (this.formIndexes.value[key].added)
                {
                    changeObj.indexChange.push(
                        {
                            from: null,
                            to: this.formIndexes.value[key]
                        }
                    );
                }
            }

            this.changes = changeObj;
            console.log(changeObj);
        }
    }

    get originalFormColumns(): FormArray
    {
        return this.originalForm.get('columns') as FormArray;
    }

    get originalFormConstraints(): FormArray
    {
        return this.originalForm.get('constraints') as FormArray;
    }

    get originalFormIndexes(): FormArray
    {
        return this.originalForm.get('indexes') as FormArray;
    }

    get formColumns(): FormArray
    {
        return this.alterTableForm.get('columns') as FormArray;
    }

    get activeFormColumns(): Array<string>
    {
        let activeCols = [];
        for (let key in this.formColumns.value)
        {
            let col = this.formColumns.value[key];
            if (!col.deleted)
            {
                activeCols.push(col.name);
            }
        }

        return activeCols;
    }

    get formConstraints(): FormArray
    {
        return this.alterTableForm.get('constraints') as FormArray;
    }

    get formIndexes(): FormArray
    {
        return this.alterTableForm.get('indexes') as FormArray;
    }

    concatSchemaTableColumn(schema: string, table: string, column: string): string
    {
        return `${schema}.${table}(${column})`;
    }

    initFields()
    {
        this.columnList.forEach(column =>
        {
            //col.FK = !isNullOrUndefined(column.referencedColumn);
            column.deleted = false;
            column.added = false;

            let group: FormGroup = this.formBuilder.group(column);
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
            this.originalFormColumns.push(this.formBuilder.group(column));
        });

        this.foreignKeys.forEach(foreignKey =>
        {
            foreignKey.deleted = false;
            foreignKey.added = false;

            this.formConstraints.push(this.formBuilder.group(foreignKey));
            this.originalFormConstraints.push(this.formBuilder.group(foreignKey));
        });

        this.indexList.forEach(index =>
        {
            index.deleted = false;
            index.added = false;

            this.formIndexes.push(this.formBuilder.group(index));
            this.originalFormIndexes.push(this.formBuilder.group(index));
        });

    }

    addColumn(name, type, defaultValue, PK, UQ, NUL, AI)
    {
        let col = Column.defaultColumn(this.schemaName, this.table.name);

        this.newColumnValidateMessage = newColumnValidator(name, type);
        if (this.newColumnValidateMessage != null)
        {
            return;
        }

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
        key.schema = this.schemaName;
        key.table = this.table.name;
        key.column = column.value;
        key.refSchema = parsedRef[0];
        key.refTable = parsedRef[1];
        key.refColumn = parsedRef[2];
        key.updateRule = updateRule.value;
        key.deleteRule = deleteRule.value;
        key.deleted = false;
        key.added = true;

        this.formConstraints.push(this.formBuilder.group(key));

        name.value = '';
        column.value = '';
        reference.value = '';
        updateRule.value = this.CASCADE_OPTIONS[0];
        deleteRule.value = this.CASCADE_OPTIONS[0];
    }

    addIndex(name, reference, unique, nullable)
    {
        let index = new Index();

        this.newIndexValidateMessage = newIndexValidator(name, reference);
        if (this.newIndexValidateMessage != null)
        {
            return;
        }

        let parsedRef = Utils.parseColumnReference(reference.value);
        index.indexSchema = this.schemaName;
        index.indexName = name.value;
        index.schema = parsedRef[0];
        index.table = parsedRef[1];
        index.column = parsedRef[2];
        index.unique = unique.checked;
        index.nullable = nullable.checked;
        index.deleted = false;
        index.added = true;

        this.formIndexes.push(this.formBuilder.group(index));

        name.value = '';
        reference.value = '';
        unique.checked = false;
        nullable.checked = true;
    }

    removeColumn(index: number)
    {
        if (index > this.originalFormColumns.length-1)
        {
            this.formColumns.removeAt(index);
        }
        else
        {
            this.formColumns.value[index].deleted = true;
            this.handleChange(this.alterTableForm.value);
        }
    }

    removeForeignKey(index: number)
    {
        if (index > this.originalFormConstraints.length-1)
        {
            this.formConstraints.removeAt(index);
        }
        else
        {
            this.formConstraints.value[index].deleted = true;
            this.handleChange(this.alterTableForm.value);
        }
    }

    removeIndex(index: number)
    {
        if (index > this.originalFormIndexes.length-1)
        {
            this.formIndexes.removeAt(index);
        }
        else
        {
            this.formIndexes.value[index].deleted = true;
            this.handleChange(this.alterTableForm.value);
        }
    }

    undoRemoveColumn(index: number)
    {
        this.formColumns.value[index].deleted = false;
        this.handleChange(this.alterTableForm.value);
    }

    undoRemoveForeignKey(index: number)
    {
        this.formConstraints.value[index].deleted = false;
        this.handleChange(this.alterTableForm.value);
    }

    undoRemoveIndex(index: number)
    {
        this.formIndexes.value[index].deleted = false;
        this.handleChange(this.alterTableForm.value);
    }

    submit()
    {
        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = this.table.name;
        modalRef.componentInstance.type = "modify";

        modalRef.result
                .then(() =>
                {
                    this.databaseService.alterTable(this.schemaName, this.table.name, this.changes)
                        .then(() => this.router.navigate(['/db'], { queryParams: {schema: this.schemaName, tab: 'table'}}))
                        .catch(error => this.errorHandler.handleError(error));
                })
                .catch(() => null);
    }
}
