import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ReactiveFormsModule} from "@angular/forms";
import {ProcedureComponent} from "./procedure.component";
import {ProcedureRoutingModule} from "./procedure-routing.module";
import {Procedure} from "../../models/rest/rest-models";

@NgModule({
    imports: [CommonModule, ProcedureRoutingModule, ReactiveFormsModule],
    declarations: [ProcedureComponent]
})
export class ProcedureModule
{
}
