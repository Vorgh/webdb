import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormArray, FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {Column, Index} from "../../../models/rest-models";
import {Table} from "../../../models/rest-models";
import {Constraint} from "../../../models/rest-models";
import {isNullOrUndefined} from "util";
import {DatabaseService} from "../../../services/database.service";

@Component({
    selector: 'alter-table-modal',
    templateUrl: './alter-table.component.html',
    styleUrls: ['./alter-table.component.scss']
})
export class AlterTableComponent implements OnInit
{
    readonly CASCADE_OPTIONS = ["RESTRICT", "CASCADE", "SET NULL", "NO ACTION"];

    @Input() table: Table;
    @Input() columnList: Column[];
    @Input() foreignKeys: Constraint[];
    @Input() indexList: Index[];

    alterTableForm: FormGroup;
    originalForm: FormGroup;
    changes: any;

    constructor(public activeModal: NgbActiveModal,
                private formBuilder: FormBuilder,
                private databaseService: DatabaseService)
    {
    }

    ngOnInit()
    {
        this.originalForm = this.formBuilder.group({
                tableName: [this.table.name],
                columns: this.formBuilder.array([]),
                constraints: this.formBuilder.array([]),
                indexes: this.formBuilder.array([])
            }
        );
        this.alterTableForm = this.formBuilder.group({
                tableName: [this.table.name],
                columns: this.formBuilder.array([]),
                constraints: this.formBuilder.array([]),
                indexes: this.formBuilder.array([])
            }
        );
        this.initFields();

        this.alterTableForm.valueChanges.subscribe(change =>
        {
            this.handleChange(change);
        })
    }

    private handleChange(change: any)
    {
        let changeObj: any = {};
        if (this.originalForm.get('tableName').value !== change['tableName'])
        {
            changeObj.nameChange = change['tableName'];
        }

        changeObj.columnChange = [];
        for (let key of Object.keys(this.formColumns.value))
        {
            if (this.formColumns.value[key].name === "" || this.formColumns.value[key].columnType === "")
                continue;

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

            this.formColumns.push(this.formBuilder.group(column));
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
        let col = Column.defaultColumn(this.table.schema, this.table.name);
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
        let parsedRef = this.parseReference(reference.value);

        if (isNullOrUndefined(parsedRef)) return;

        key.constraintName = name.value;
        key.schema = this.table.schema;
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
        let parsedRef = this.parseReference(reference.value);

        if (isNullOrUndefined(parsedRef)) return;

        index.indexSchema = this.table.schema;
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

    submit()
    {
        this.databaseService.alterTable(this.table.schema, this.table.name, this.changes)
            .catch(error => console.log(error));
    }

    private parseReference(reference: string): any[]
    {
        let regex = new RegExp('^[A-z]+[.][A-z]+[(][A-z]+[)]');
        if (!regex.test(reference))
        {
            console.exception('Wrong pattern! Please correct it to match: \'schema_name\'.\'table_name\'(column_name)');
            return null;
        }

        let parsed = [];
        parsed.push(reference.substring(0, reference.indexOf(".")));
        parsed.push(reference.substring(reference.indexOf(".")+1, reference.indexOf("(")));
        parsed.push(reference.substring(reference.indexOf("(")+1, reference.indexOf(")")));

        return parsed;
    }
}
