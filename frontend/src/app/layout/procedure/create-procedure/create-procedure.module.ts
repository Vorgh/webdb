import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule} from "@angular/forms";
import { CreateProcedureComponent } from './create-procedure.component';
import {PageHeaderModule} from "../../../shared/modules";
import {CreateProcedureRoutingModule} from "./create-procedure-routing.module";
import {MonacoEditorModule} from "ngx-monaco-editor";

@NgModule({
  imports: [CommonModule, CreateProcedureRoutingModule, ReactiveFormsModule, PageHeaderModule, MonacoEditorModule],
  declarations: [CreateProcedureComponent]
})
export class CreateProcedureModule { }
