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

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        AppRoutingModule,
        HttpInterceptorModule
    ],
    declarations: [AppComponent],
    providers: [AuthGuard, ConnectionService, DatabaseService, PageHeaderService],
    bootstrap: [AppComponent]
})
export class AppModule {}
