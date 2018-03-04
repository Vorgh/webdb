import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {TriggerComponent} from "./trigger.component";
import {TriggerRoutingModule} from "./trigger-routing.module";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
    imports: [CommonModule, TriggerRoutingModule, ReactiveFormsModule],
    declarations: [TriggerComponent]
})
export class TriggerModule {}
