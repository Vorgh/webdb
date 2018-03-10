import {Component, OnInit} from '@angular/core';
import {Trigger} from "../../models/rest-models";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DatabaseService} from "../../services/database.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Utils} from "../../shared/util/utils";
import {tableReferenceValidator} from "../../shared/validator/table-reference.validator";
import {triggerBodyValidator} from "../../shared/validator/trigger-body.validator";

@Component({
    selector: 'app-trigger',
    templateUrl: './trigger.component.html',
    styleUrls: ['./trigger.component.scss']
})
export class TriggerComponent implements OnInit
{
    readonly TIMINGS = ["BEFORE", "AFTER"];
    readonly EVENTS = ["INSERT", "UPDATE", "DELETE"];

    schema: string;
    trigger: Trigger;
    triggerForm: FormGroup;

    constructor(private formBuilder: FormBuilder,
                private databaseService: DatabaseService,
                private pageHeaderService: PageHeaderService,
                private router: Router,
                private route: ActivatedRoute)
    {
    }

    ngOnInit()
    {
        this.route.data.subscribe((data: {trigger: Trigger}) =>
        {
            this.trigger = data.trigger;
            this.schema = data.trigger.schema;

            this.pageHeaderService.addFragment('modify-trigger', this.pageHeaderService.getHeaderByID('dbhome'),
                this.router.url, 'Modify Trigger', 'fa-table');

            this.triggerForm = this.formBuilder.group({
                    triggerName: [this.trigger.name, Validators.required],
                    triggerTiming: [this.trigger.timing, Validators.required],
                    triggerEvent: [this.trigger.eventType, Validators.required],
                    triggerEventTarget: [this.trigger.eventSchema + "." + this.trigger.eventTable, tableReferenceValidator()],
                    triggerBody: [this.trigger.triggerBody, triggerBodyValidator()],
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

        this.databaseService.modifyTrigger(this.schema, trigger)
            .then(() => this.router.navigate(['/db']))
            .catch(error => console.log(error));
    }
}
