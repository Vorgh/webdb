import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from "@angular/common/http";
import { AppRoutingModule } from './modules/routing.module';
import { FormsModule, ReactiveFormsModule }   from '@angular/forms';
import {NgModule} from '@angular/core';


import {AppComponent} from './app.component';
import { ConnectionComponent } from './components/connection/connection.component';
import { HomeComponent } from './components/home/home.component';
import {ConnectionService} from "./services/connection.service";
import {DatabaseService} from "./services/database.service";
import {HttpInterceptorModule} from "./modules/http-interceptor.module";


@NgModule({
  declarations: [
    AppComponent,
    ConnectionComponent,
    HomeComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpInterceptorModule
  ],
  providers: [ConnectionService, DatabaseService],
  bootstrap: [AppComponent]
})
export class AppModule
{
}
