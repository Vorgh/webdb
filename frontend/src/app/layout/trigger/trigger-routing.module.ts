import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {TriggerComponent} from "./trigger.component";
import {TriggerResolver} from "./trigger-resolver.service";

const routes: Routes = [
    {path: '', component: TriggerComponent, resolve: {trigger: TriggerResolver}, runGuardsAndResolvers: 'paramsOrQueryParamsChange'},
    {path: 'create', loadChildren: './create-trigger/create-trigger.module#CreateTriggerModule'},
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TriggerRoutingModule {
}
