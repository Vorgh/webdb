import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AuthGuard, HttpRequestInterceptorModule} from './shared';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
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
import {AlterViewResolver} from "./layout/view/alter-view/alter-view-resolver.service";
import {MonacoEditorModule, NgxMonacoEditorConfig} from "ngx-monaco-editor";
import {Utils} from "./shared/util/utils";
import {conf, language} from "../assets/monaco/languages/mysql/mysql";
import {DbModalService} from "./services/db-modal.service";

export function monacoLoad()
{
    const monaco = window['monaco'];
    monaco.languages.register({
        id: 'mysql',
        extensions: [],
        aliases: ['MySQL', 'mysql']
    });
    monaco.languages.setMonarchTokensProvider('mysql', language);
    monaco.languages.setLanguageConfiguration('mysql', conf);
    monaco.languages.registerCompletionItemProvider('mysql', {
        provideCompletionItems: function() {
            return Utils.getMonacoCompletionProvider()
        }
    });

    monaco.editor.defineTheme('mysqlTheme', {
        base: 'vs', // can also be vs-dark or hc-black
        inherit: true, // can also be false to completely replace the builtin rules
        rules: [
            { token: 'identifier.quote', foreground: 'ffa500'},
    ]});
}

const monacoConfig: NgxMonacoEditorConfig = {
    onMonacoLoad: monacoLoad
};

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        AppRoutingModule,
        HttpRequestInterceptorModule,
        NgbModule.forRoot(),
        MonacoEditorModule.forRoot(monacoConfig)
    ],
    declarations: [AppComponent],
    providers: [AuthGuard, ConnectionService, DatabaseService, PageHeaderService, AlterTableResolver,
        TriggerResolver, ProcedureResolver, AlterViewResolver, DBHomeResolver, NotificationService,
        CookieService, GlobalErrorHandler, DbModalService],
    bootstrap: [AppComponent]
})
export class AppModule
{
}
