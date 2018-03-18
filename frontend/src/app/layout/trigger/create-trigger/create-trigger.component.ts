import {Component, OnInit} from '@angular/core';
import {PageHeaderService} from "../../../shared/modules/page-header/page-header.service";
import {isNullOrUndefined} from "util";
import {ActivatedRoute, Router} from "@angular/router";
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Utils} from "../../../shared/util/utils";
import {DatabaseService} from "../../../services/database.service";
import {Trigger} from "../../../models/rest-models";
import {tableReferenceValidator} from "../../../shared/validator/table-reference.validator";
import {bodyValidator} from "../../../shared/validator/body.validator";
import {GlobalErrorHandler} from "../../../shared/error-handler/error-handler.service";

@Component({
    selector: 'app-create-trigger',
    templateUrl: './create-trigger.component.html',
    styleUrls: ['./create-trigger.component.scss']
})
export class CreateTriggerComponent implements OnInit
{
    readonly TIMINGS = ["BEFORE", "AFTER"];
    readonly EVENTS = ["INSERT", "UPDATE", "DELETE"];

    schema: string;
    createTriggerForm: FormGroup;

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
        this.route.queryParams.subscribe(params =>
        {
            if (!isNullOrUndefined(params['schema']))
            {
                this.schema = params['schema'];

                this.pageHeaderService.addFragment('create-trigger', this.pageHeaderService.getHeaderByID('dbhome'),
                    this.router.url, 'New Trigger', 'fa-table');
            }
            else
            {
                this.router.navigate(['/home']);
            }
        });

        this.createTriggerForm = this.formBuilder.group({
                triggerName: ['', Validators.required],
                triggerTiming: [this.TIMINGS[0], Validators.required],
                triggerEvent: [this.EVENTS[0], Validators.required],
                triggerEventTarget: ['', tableReferenceValidator()],
                triggerBody: ['BEGIN\n\nEND;', bodyValidator()],
            }
        );
    }

    submit()
    {
        let trigger: Trigger = new Trigger();
        trigger.schema = this.schema;
        trigger.name = this.createTriggerForm.get('triggerName').value;
        trigger.timing = this.createTriggerForm.get('triggerTiming').value;
        trigger.eventType = this.createTriggerForm.get('triggerEvent').value;
        let target = Utils.parseTableReference(this.createTriggerForm.get('triggerEventTarget').value);
        trigger.eventSchema = target[0];
        trigger.eventTable = target[1];
        trigger.triggerBody = this.createTriggerForm.get('triggerBody').value;

        this.databaseService.createTrigger(this.schema, trigger)
            .then(() => this.router.navigate(['/db']))
            .catch(this.errorHandler.handleError);
    }

    getFormControl(name: string): AbstractControl
    {
        return this.createTriggerForm.get(name);
    }
}
