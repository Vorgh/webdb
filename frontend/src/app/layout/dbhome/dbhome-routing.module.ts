import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DBHomeComponent } from "./dbhome.component";
import {DBHomeResolver} from "./dbhome-resolver.service";

const routes: Routes = [
    { path: '', component: DBHomeComponent, resolve: {dbData: DBHomeResolver} }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DBHomeRoutingModule {
}
