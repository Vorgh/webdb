import {NgModule}             from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ConnectionComponent} from "../components/connection/connection.component";
import {HomeComponent} from "../components/home/home.component";

const routes: Routes = [
  {path: 'connect', component: ConnectionComponent},
  {path: 'home', component: HomeComponent},
  {path: '**', redirectTo: '/connect'} //Ez mindig az utolsó legyen!
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
