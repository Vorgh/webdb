import {AfterViewInit, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {PageHeaderService} from "../../../shared/modules/page-header/page-header.service";
import {isNullOrUndefined} from "util";
import {ActivatedRoute, Router} from "@angular/router";
import {AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Utils} from "../../../shared/util/utils";
import {DatabaseService} from "../../../services/database.service";
import {Parameter, Procedure} from "../../../models/rest/rest-models";
import {bodyValidator} from "../../../shared/validator/body.validator";
import {newParameterValidator} from "../../../shared/validator/add-new.validator";
import {columnTypeValidator} from "../../../shared/validator/column-type.validator";
import {GlobalErrorHandler} from "../../../services/error-handler.service";
import {procedureReturnTypeValidator} from "../../../shared/validator/procedure-return-type.validator";
import {ConfirmdialogComponent} from "../../components/confirmdialog/confirmdialog.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'app-create-procedure',
    templateUrl: './create-procedure.component.html',
    styleUrls: ['./create-procedure.component.scss']
})
export class CreateProcedureComponent implements OnInit, AfterViewInit
{
    readonly RETURN_TYPES = [""].concat(Utils.dataTypes);
    readonly PARAMETER_MODES = ["IN", "OUT", "INOUT"];

    schema: string;
    procedureName: string;
    createProcedureForm: FormGroup;
    newParameterValidateMessage: string;

    pageHeaderPath = [];
    editorOptions = {
        theme: 'mysqlTheme',
        language: 'mysql',
        autocomplete: true,
        automaticLayout: true,
        minimap: {enabled: false},
        scrollBeyondLastLine: false,
        fontSize: 16,
        formatOnPaste: true};

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
        this.route.queryParams.subscribe(params =>
        {
            if (!isNullOrUndefined(params['schema']))
            {
                this.schema = params['schema'];
                this.procedureName = params['procedure'];

                this.pageHeaderPath = this.pageHeaderService.getPathFromID('create-procedure', this.schema);
            }
            else
            {
                this.errorHandler.notFound();
            }
        });

        this.createProcedureForm = this.formBuilder.group({
                procedureName: ['', Validators.required],
                procedureReturnType: [this.RETURN_TYPES[0], procedureReturnTypeValidator()],
                procedureParameters: this.formBuilder.array([]),
                procedureBody: ['BEGIN\n\nEND', bodyValidator()]
            }
        );

        this.getFormControl('procedureName').valueChanges.subscribe(value =>
        {
            this.getFormArray('procedureParameters').controls.forEach(control =>
            {
                control.value.procedureName = value;
            });
        });

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
    }

    ngAfterViewInit()
    {
        this.checkAndDisableParameterModeInputs();
        this.changeDetector.detectChanges();
    }

    submit()
    {
        let procedure: Procedure = new Procedure();

        procedure.schema = this.schema;
        procedure.name = this.createProcedureForm.get('procedureName').value;
        procedure.returnType = this.createProcedureForm.get('procedureReturnType').value;
        procedure.type = procedure.returnType == null || procedure.returnType == '' ? "PROCEDURE" : "FUNCTION";
        procedure.body = this.createProcedureForm.get('procedureBody').value;
        procedure.paramList = this.getFormArray('procedureParameters').controls.map(control => control.value);

        const modalRef = this.modalService.open(ConfirmdialogComponent);
        modalRef.componentInstance.dbObject = procedure.name;
        modalRef.componentInstance.type = "create";

        modalRef.result
                .then(() =>
                {
                    this.databaseService.createProcedure(procedure)
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
                })
                .catch(() => null);
    }

    getFormControl(name: string): AbstractControl
    {
        return this.createProcedureForm.get(name);
    }

    getFormArray(name: string): FormArray
    {
        return this.createProcedureForm.get(name) as FormArray;
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
        param.procedureName = this.procedureName;
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
        paramArray.removeAt(index);

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
