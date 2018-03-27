import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ProcedureComponent} from "./procedure.component";
import {ProcedureResolver} from "./procedure-resolver.service";

const routes: Routes = [
    {path: '', component: ProcedureComponent, resolve: {procedure: ProcedureResolver}, runGuardsAndResolvers: 'paramsOrQueryParamsChange'},
    {path: 'create', loadChildren: './create-procedure/create-procedure.module#CreateProcedureModule'},
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ProcedureRoutingModule {
}
