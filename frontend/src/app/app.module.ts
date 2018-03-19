import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthGuard, HttpRequestInterceptorModule} from './shared';
import {ConnectionService} from "./services/connection.service";
import {DatabaseService} from "./services/database.service";
import {PageHeaderService} from "./shared/modules/page-header/page-header.service";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {AlterTableResolver} from "./layout/table/alter-table/alter-table-resolver.service";
import {TriggerResolver} from "./layout/trigger/trigger-resolver.service";
import {ProcedureResolver} from "./layout/procedure/procedure-resolver.service";
import {CookieService} from "ngx-cookie-service";
import {GlobalErrorHandler} from "./services/error-handler.service";
import {DBHomeResolver} from "./layout/dbhome/dbhome-resolver.service";
import {NotificationService} from "./services/notification.service";

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        AppRoutingModule,
        HttpRequestInterceptorModule,
        NgbModule.forRoot()
    ],
    declarations: [AppComponent],
    providers: [AuthGuard, ConnectionService, DatabaseService, PageHeaderService, AlterTableResolver,
        TriggerResolver, ProcedureResolver, DBHomeResolver, NotificationService,
        CookieService, GlobalErrorHandler],
    bootstrap: [AppComponent]
})
export class AppModule
{
}
