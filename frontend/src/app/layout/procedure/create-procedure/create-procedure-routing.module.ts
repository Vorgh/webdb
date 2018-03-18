import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {CreateProcedureComponent} from "./create-procedure.component";

const routes: Routes = [
    {path: '', component: CreateProcedureComponent}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CreateProcedureRoutingModule {
}
