import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthGuard, HttpInterceptorModule } from './shared';
import {ConnectionService} from "./services/connection.service";
import {DatabaseService} from "./services/database.service";
import {PageHeaderService} from "./shared/modules/page-header/page-header.service";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {AlterTableResolver} from "./layout/table/alter-table/alter-table-resolver.service";
import {TriggerResolver} from "./layout/trigger/trigger-resolver.service";

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        AppRoutingModule,
        HttpInterceptorModule,
        NgbModule.forRoot()
    ],
    declarations: [AppComponent],
    providers: [AuthGuard, ConnectionService, DatabaseService, PageHeaderService, AlterTableResolver, TriggerResolver],
    bootstrap: [AppComponent]
})
export class AppModule {}
