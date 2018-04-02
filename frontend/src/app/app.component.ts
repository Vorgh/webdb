import {Component, OnInit} from '@angular/core';
import {Utils} from "./shared/util/utils";
import {conf, language} from "../assets/monaco/languages/mysql/mysql";
import {NgxMonacoEditorConfig} from "ngx-monaco-editor";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit
{
    constructor()
    {
    }

    ngOnInit()
    {
    }
}
