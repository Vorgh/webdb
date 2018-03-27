import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AlterViewComponent} from "./alter-view.component";
import {AlterViewResolver} from "./alter-view-resolver.service";

const routes: Routes = [
    { path: '', component: AlterViewComponent, resolve: {view: AlterViewResolver}, runGuardsAndResolvers: 'paramsOrQueryParamsChange' }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AlterViewRoutingModule {
}
