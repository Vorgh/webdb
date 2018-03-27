import {AfterContentInit, AfterViewInit, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Utils} from "../../shared/util/utils";
import {DatabaseService} from "../../services/database.service";
import {ActivatedRoute, Router} from "@angular/router";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {Parameter, Procedure} from "../../models/rest/rest-models";
import {bodyValidator} from "../../shared/validator/body.validator";
import {isNullOrUndefined} from "util";
import {newParameterValidator} from "../../shared/validator/add-new.validator";
import {columnTypeValidator} from "../../shared/validator/column-type.validator";
import {GlobalErrorHandler} from "../../services/error-handler.service";
import {ModifyRequest} from "../../models/request/request-models";
import {procedureReturnTypeValidator} from "../../shared/validator/procedure-return-type.validator";
import {ConfirmdialogComponent} from "../components/confirmdialog/confirmdialog.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-procedure',
    templateUrl: './procedure.component.html',
    styleUrls: ['./procedure.component.scss']
})
export class ProcedureComponent implements OnInit, AfterViewInit
{
    readonly RETURN_TYPES = [""].concat(Utils.dataTypes);
    readonly PARAMETER_MODES = ["IN", "OUT", "INOUT"];

    private originalParamCount: number;

    schema: string;
    originalProcedure: Procedure;
    procedureForm: FormGroup;
    newParameterValidateMessage: string;

    constructor(private formBuilder: FormBuilder,
                private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private router: Router,
                private route: ActivatedRoute,
                private errorHandler: GlobalErrorHandler,
                private changeDetector: ChangeDetectorRef,
                private modalService: NgbModal)
    {
    }

    ngOnInit()
    {
        this.route.data.subscribe((data: {procedure: Procedure}) =>
        {
            this.originalProcedure = data.procedure;
            this.schema = data.procedure.schema;
            this.originalParamCount = isNullOrUndefined(this.originalProcedure.paramList) ? 0 : this.originalProcedure.paramList.length;

            this.pageHeaderService.addFragment('modify-procedure', this.pageHeaderService.getHeaderByID('dbhome'),
                this.router.url, 'Modify Procedure', 'fa-table');

            let procedureReturnType = this.originalProcedure.returnType != null ?
                this.originalProcedure.returnType: '';
            this.procedureForm = this.formBuilder.group({
                    procedureName: [this.originalProcedure.name, Validators.required],
                    procedureReturnType: [procedureReturnType, procedureReturnTypeValidator()],
                    procedureParameters: this.formBuilder.array([]),
                    procedureBody: [this.originalProcedure.body, bodyValidator()]
                }
            );

            this.getFormControl('procedureName').valueChanges.subscribe(value =>
            {
                this.getFormArray('procedureParameters').controls.forEach(control =>
                {
                    control.value.procedureName = value;
                });
            });

            if (!isNullOrUndefined(this.originalProcedure.paramList))
            {
                let parameterArray: FormArray = this.procedureForm.get('procedureParameters') as FormArray;
                for (let param of this.originalProcedure.paramList)
                {
                    let group: FormGroup = this.formBuilder.group({
                        name: param.name,
                        type: param.type,
                        mode: param.mode
                    });
                    parameterArray.push(group);
                }
            }

            this.getFormControl('procedureReturnType').valueChanges.subscribe(value =>
            {
                if (isNullOrUndefined(value) || value == '')
                {
                    this.getFormArray('procedureParameters').controls.forEach(control =>
                    {
                        control.get('mode').enable()
                    });
                }
                else
                {
                    this.getFormArray('procedureParameters').controls.forEach(control =>
                    {
                        control.get('mode').disable();
                    });
                }
            });
        });
    }

    ngAfterViewInit()
    {
        this.checkAndDisableParameterModeInputs()
        this.changeDetector.detectChanges();
    }

    submit()
    {
        let procedure: Procedure = new Procedure();

        procedure.schema = this.schema;
        procedure.name = this.procedureForm.get('procedureName').value;
        procedure.returnType = this.procedureForm.get('procedureReturnType').value;
        procedure.type = procedure.returnType == null || procedure.returnType == '' ? "PROCEDURE" : "FUNCTION";
        procedure.body = this.procedureForm.get('procedureBody').value;
        procedure.paramList = this.getFormArray('procedureParameters').controls.map(control => control.value);

        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = procedure.name;
        modalRef.componentInstance.type = "modify";

        modalRef.result.then(() =>
        {
            let request: ModifyRequest<Procedure> = new ModifyRequest<Procedure>(this.originalProcedure, procedure);
            this.databaseService.modifyProcedure(request)
                .then(() =>
                {
                    let redirectTab: string;
                    if (isNullOrUndefined(procedure.returnType) || procedure.returnType == '')
                    {
                        redirectTab = 'procedures';
                    }
                    else
                    {
                        redirectTab = 'functions';
                    }

                    this.router.navigate(['/db'], {queryParams: {schema: this.schema, tab: redirectTab}})
                })
                .catch(error => this.errorHandler.handleError(error));
        });
    }

    getFormControl(name: string): AbstractControl
    {
        return this.procedureForm.get(name);
    }

    getFormArray(name: string): FormArray
    {
        return this.procedureForm.get(name) as FormArray;
    }

    addParameter(name, type, mode)
    {
        let param = new Parameter();

        this.newParameterValidateMessage = newParameterValidator(name, type);
        if (this.newParameterValidateMessage != null)
        {
            return;
        }

        param.schema = this.schema;
        param.procedureName = this.originalProcedure.name;
        param.name = name.value;
        param.type = type.value;
        param.mode = mode.value;
        param.deleted = false;
        param.added = true;

        this.addParameterControl(param);

        name.value = "";
        type.value = "";
        mode.value = this.PARAMETER_MODES[0];
    }

    private addParameterControl(param: Parameter)
    {
        let group: FormGroup = this.formBuilder.group(param);
        for (let key of Object.keys(group.value))
        {
            let control = group.get(key);
            switch (key)
            {
                case "name":
                    control.setValidators(Validators.required);
                    break;
                case "type":
                    control.setValidators([Validators.required, columnTypeValidator()]);
                    break;
                case "mode":
                    let returnTypeValue = this.getFormControl('procedureReturnType').value;
                    if (returnTypeValue != null && returnTypeValue != '')
                    {
                        control.disable();
                    }
                    break;
            }
        }

        this.getFormArray('procedureParameters').push(group);
    }

    removeParameter(index: number)
    {
        let paramArray: FormArray = this.getFormControl('procedureParameters') as FormArray;
        if (index > this.originalParamCount-1)
        {
            paramArray.removeAt(index);
        }
        else
        {
            paramArray.value[index].deleted = true;
        }
    }

    undoRemoveParameter(index: number)
    {
        let paramArray: FormArray = this.getFormControl('procedureParameters') as FormArray;
        paramArray.value[index].deleted = false;
    }

    private checkAndDisableParameterModeInputs()
    {
        let returnTypeValue = this.getFormControl('procedureReturnType').value;
        if (returnTypeValue == null || returnTypeValue == '')
        {
            this.getFormArray('procedureParameters').controls.forEach(control =>
            {
                control.get('mode').enable()
            });
        }
        else
        {
            this.getFormArray('procedureParameters').controls.forEach(control =>
            {
                control.get('mode').disable();
            });
        }
    }
}
