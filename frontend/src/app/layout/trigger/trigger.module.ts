import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {TriggerComponent} from "./trigger.component";
import {TriggerRoutingModule} from "./trigger-routing.module";
import {ReactiveFormsModule} from "@angular/forms";
import {PageHeaderModule} from "../../shared/modules";
import {MonacoEditorModule} from "ngx-monaco-editor";

@NgModule({
    imports: [CommonModule, TriggerRoutingModule, ReactiveFormsModule, PageHeaderModule, MonacoEditorModule],
    declarations: [TriggerComponent]
})
export class TriggerModule {}
