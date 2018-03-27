import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ViewComponent} from "./view.component";

const routes: Routes = [
    {path: '', component: ViewComponent},
    {path: 'create', loadChildren: './create-view/create-view.module#CreateViewModule'},
    {path: 'alter', loadChildren: './alter-view/alter-view.module#AlterViewModule'}
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ViewRoutingModule {
}
