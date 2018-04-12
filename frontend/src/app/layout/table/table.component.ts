import {Component, Input, OnInit} from '@angular/core';
import {routerTransition} from '../../router.animations';
import {Column, Row} from "../../models/rest/rest-models";
import {DatabaseService} from "../../services/database.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {isNullOrUndefined, isNumber} from "util";
import {Table} from "../../models/rest/rest-models";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {GlobalErrorHandler} from "../../services/error-handler.service";
import {ConfirmdialogComponent} from "../components/confirmdialog/confirmdialog.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-table',
    templateUrl: './table.component.html',
    styleUrls: ['./table.component.scss'],
    animations: [routerTransition()]
})
export class TableComponent implements OnInit
{
    columns: Column[];
    rows: Row[];
    metadata: Table;
    schema: string;
    table: string;
    rowForm: FormGroup;
    newRowGroup: FormGroup;

    pageHeaderPath = [];

    constructor(private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private route: ActivatedRoute,
                private router: Router,
                private formBuilder: FormBuilder,
                private errorHandler: GlobalErrorHandler,
                private modalService: NgbModal)
    {
        this.columns = [];
        this.rows = [];
        this.metadata = new Table();
        this.rowForm = this.formBuilder.group([]);
        this.newRowGroup = this.formBuilder.group([]);
    }

    ngOnInit()
    {
        this.rowForm.addControl("newRow", this.newRowGroup);
        this.route.queryParams.subscribe(params =>
        {
            if (!isNullOrUndefined(params['schema']) && !isNullOrUndefined(params['table']))
            {
                this.schema = params['schema'];
                this.table = params['table'];

                Promise.all([
                    this.databaseService.getColumns(this.schema, this.table),
                    this.databaseService.getRows(this.schema, this.table),
                    this.databaseService.getTable(this.schema, this.table)
                ])
                       .then(values =>
                       {
                           this.columns = values[0];
                           this.rows = values[1];
                           this.metadata = values[2];

                           this.pageHeaderPath = this.pageHeaderService.getPathFromID('table', null, this.metadata);

                           let index = 0;
                           for (let row of this.rows)
                           {
                               let group: FormGroup = this.formBuilder.group({
                                   added: false,
                                   deleted: false
                               });
                               for (let col of this.columns)
                               {
                                   if (!col.autoIncrement)
                                       group.addControl(col.name, new FormControl(row[col.name]));
                                   else
                                       group.addControl(col.name, new FormControl({
                                           value: row[col.name],
                                           disabled: true
                                       }));
                               }
                               this.rowForm.addControl(index.toString(), group);
                               index++;
                           }

                           for (let col of this.columns)
                           {
                               if (!col.autoIncrement)
                                   this.newRowGroup.addControl(col.name, new FormControl(""));
                               else
                                   this.newRowGroup.addControl(col.name, new FormControl({value: "", disabled: true}));
                           }
                       })
                       .catch(error => this.errorHandler.handleError(error));
            }
            else
            {
                this.errorHandler.notFound();
            }
        });
    }

    addRow()
    {
        let group: FormGroup = this.formBuilder.group({
            added: true,
            deleted: false
        });
        group.markAsDirty();
        let newRow: Row = new Row();
        for (let col of this.columns)
        {
            if (col.autoIncrement)
            {
                let length: number = Object.keys(this.rowForm.value).length;
                if (length > 1)
                {
                    const autoIndex = this.findMaxAutoIncrement(this.rowForm, col.name);
                    this.newRowGroup.get(col.name)
                        .setValue(autoIndex + 1);
                }
                else
                    this.newRowGroup.get(col.name)
                        .setValue(1);

                group.addControl(col.name, new FormControl({
                    value: this.newRowGroup.get(col.name).value,
                    disabled: true
                }));
            }
            else
            {
                group.addControl(col.name, new FormControl(this.newRowGroup.get(col.name).value));
            }
            newRow[col.name] = this.newRowGroup.get(col.name).value;
        }
        newRow.added = true;
        this.rowForm.addControl((Object.keys(this.rowForm.value).length - 1).toString(), group);
        this.rows.push(newRow);
        this.newRowGroup.reset();
    }

    deleteRow(index: number)
    {
        if (this.rows[index].added)
        {
            this.rows.splice(index, 1);
            this.rowForm.removeControl(index.toString());

            let currentIndex = index;
            Object.keys(this.rowForm.value)
                  .filter(row => +row > index)
                  .forEach(key =>
                  {
                      let control = new FormControl(this.rowForm.get(key));
                      this.rowForm.removeControl(key);
                      this.rowForm.addControl(currentIndex.toString(), control.value);
                      currentIndex++;
                  });
        }
        else
        {
            this.rows[index].deleted = true;
            this.rowForm.get(index.toString())
                .patchValue({deleted: true});
            this.rowForm.get(index.toString())
                .markAsDirty();
        }
    }

    undoDeleteRow(index: number)
    {
        this.rows[index].deleted = false;
        this.rowForm.get(index.toString())
            .patchValue({deleted: false});
    }

    onSubmit()
    {
        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = this.table;
        modalRef.componentInstance.type = "modify";

        modalRef.result
                .then(() =>
                {
                    let changeObj: { changes: any } = {changes: []};
                    this.newRowGroup.reset();

                    const rawFormValue = this.rowForm.getRawValue();
                    for (let key of Object.keys(rawFormValue))
                    {
                        let row = rawFormValue[key];

                        if (row.added)
                        {
                            changeObj.changes.push({from: null, to: this.buildChangeObject(row)});
                            continue;
                        }

                        if (row.deleted)
                        {
                            changeObj.changes.push({from: this.buildChangeObject(row), to: null});
                            continue;
                        }

                        if (this.rowForm.get(key).dirty)
                        {
                            let change = this.buildChangeObject(row);
                            changeObj.changes.push({from: change, to: change});
                        }
                    }

                    this.databaseService.modifyRows(this.schema, this.table, changeObj)
                        .then(() => this.router.navigate(['/db'], {queryParams: {schema: this.schema, tab: 'table'}}))
                        .catch(error => this.errorHandler.handleError(error));
                })
                .catch(() => null);
    }

    private findMaxAutoIncrement(from: FormGroup, propertyKey: any): number
    {
        let max: number = -1;
        const rawValue = from.getRawValue();
        for (let key of Object.keys(rawValue))
        {
            let val = rawValue[key][propertyKey];
            if (isNumber(val))
            {
                if (val > max)
                    max = val;
            }
        }

        return max;
    }

    private buildChangeObject(obj): any
    {
        let changeObj = {};

        for (let key of Object.keys(obj))
        {
            if (key != "added" && key != "deleted")
            {
                changeObj[key] = obj[key];
            }
        }

        return changeObj;
    }
}
