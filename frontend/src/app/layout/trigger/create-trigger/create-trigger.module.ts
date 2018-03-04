import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule} from "@angular/forms";
import { CreateTriggerComponent } from './create-trigger.component';
import {PageHeaderModule} from "../../../shared/modules";
import {CreateTriggerRoutingModule} from "./create-trigger-routing.module";

@NgModule({
  imports: [CommonModule, CreateTriggerRoutingModule, ReactiveFormsModule, PageHeaderModule],
  declarations: [CreateTriggerComponent]
})
export class CreateTriggerModule { }
