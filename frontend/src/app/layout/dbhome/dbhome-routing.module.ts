import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DBHomeComponent } from "./dbhome.component";

const routes: Routes = [
    { path: '', redirectTo: '/not-found', pathMatch: 'full' },
    { path: ':schema', component: DBHomeComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DBHomeRoutingModule {
}
