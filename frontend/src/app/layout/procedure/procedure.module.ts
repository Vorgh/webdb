import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ReactiveFormsModule} from "@angular/forms";
import {ProcedureComponent} from "./procedure.component";
import {ProcedureRoutingModule} from "./procedure-routing.module";
import {PageHeaderModule} from "../../shared/modules";
import {MonacoEditorModule} from "ngx-monaco-editor";

@NgModule({
    imports: [CommonModule, ProcedureRoutingModule, ReactiveFormsModule, PageHeaderModule, MonacoEditorModule],
    declarations: [ProcedureComponent]
})
export class ProcedureModule
{
}
