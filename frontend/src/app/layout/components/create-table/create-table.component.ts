import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {Table} from "../../../models/rest-models";

@Component({
    selector: 'create-table-modal',
    templateUrl: './create-table.component.html',
    styleUrls: ['./create-table.component.scss']
})
export class CreateTableComponent implements OnInit
{
    @Input() tableList: Table[];
    createTableForm: FormGroup;
    FKStates: boolean[] = [];

    constructor(public activeModal: NgbActiveModal,
                private formBuilder: FormBuilder)
    {
    }

    ngOnInit()
    {
        this.createTableForm = this.formBuilder.group({
                tableName: [''],
                columns: this.formBuilder.array([])
            }
        );
        this.addColumn();
    }

    get formColumns(): FormArray
    {
        return this.createTableForm.get('columns') as FormArray;
    }

    get columnDef()
    {
        return {
            columnName: [''],
            type: [''],
            default: [''],
            PK: false,
            UQ: false,
            NN: false,
            ZF: false,
            AI: false,
            FK: false,
            refSelect: ['']
        };
    }

    addColumn()
    {
        this.FKStates.push(false);
        this.formColumns.push(this.formBuilder.group(this.columnDef));
    }

    removeColumn(index: number)
    {
        this.formColumns.removeAt(index);
        this.FKStates.splice(index, 1);
    }

    toggleFK(index: number)
    {
        this.FKStates[index] = !this.FKStates[index];
    }
}
