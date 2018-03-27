import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AlterTableComponent } from "./alter-table.component";
import {AlterTableResolver} from "./alter-table-resolver.service";

const routes: Routes = [
    {path: '', component: AlterTableComponent, resolve: {table: AlterTableResolver}, runGuardsAndResolvers: 'paramsOrQueryParamsChange'}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AlterTableRoutingModule {
}
