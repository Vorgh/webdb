import {Component, OnInit} from '@angular/core';
import {Trigger} from "../../models/rest-models";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DatabaseService} from "../../services/database.service";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Utils} from "../../shared/util/utils";
import {tableReferenceValidator} from "../../shared/validator/table-reference.validator";
import {bodyValidator} from "../../shared/validator/body.validator";
import {ModifyTriggerRequest} from "../../models/request/request-models";
import {GlobalErrorHandler} from "../../shared/error-handler/error-handler.service";

@Component({
    selector: 'app-trigger',
    templateUrl: './trigger.component.html',
    styleUrls: ['./trigger.component.scss']
})
export class TriggerComponent implements OnInit
{
    readonly TIMINGS = ["BEFORE", "AFTER"];
    readonly EVENTS = ["INSERT", "UPDATE", "DELETE"];

    private originalTrigger: Trigger;

    schema: string;
    triggerForm: FormGroup;

    constructor(private formBuilder: FormBuilder,
                private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private router: Router,
                private route: ActivatedRoute,
                private errorHandler: GlobalErrorHandler)
    {
    }

    ngOnInit()
    {
        this.route.data.subscribe((data: {trigger: Trigger}) =>
        {
            this.originalTrigger = data.trigger;
            this.schema = data.trigger.schema;

            this.pageHeaderService.addFragment('modify-trigger', this.pageHeaderService.getHeaderByID('dbhome'),
                this.router.url, 'Modify Trigger', 'fa-table');

            this.triggerForm = this.formBuilder.group({
                    triggerName: [this.originalTrigger.name, Validators.required],
                    triggerTiming: [this.originalTrigger.timing, Validators.required],
                    triggerEvent: [this.originalTrigger.eventType, Validators.required],
                    triggerEventTarget: [this.originalTrigger.eventSchema + "." + this.originalTrigger.eventTable, tableReferenceValidator()],
                    triggerBody: [this.originalTrigger.triggerBody, bodyValidator()],
                }
            );
        });
    }

    submit()
    {
        let trigger: Trigger = new Trigger();

        trigger.schema = this.schema;
        trigger.name = this.triggerForm.get('triggerName').value;
        trigger.timing = this.triggerForm.get('triggerTiming').value;
        trigger.eventType = this.triggerForm.get('triggerEvent').value;
        let target = Utils.parseTableReference(this.triggerForm.get('triggerEventTarget').value);
        trigger.eventSchema = target[0];
        trigger.eventTable = target[1];
        trigger.triggerBody = this.triggerForm.get('triggerBody').value;

        let request: ModifyTriggerRequest = new ModifyTriggerRequest(this.originalTrigger, trigger);
        this.databaseService.modifyTrigger(request)
            .then(() => this.router.navigate(['/db'], {queryParams: {schema: this.schema, tab: 'trigger'}}))
            .catch(this.errorHandler.handleError);
    }

    getFormControl(name: string): AbstractControl
    {
        return this.triggerForm.get(name);
    }
}
