import {Component, OnInit} from '@angular/core';

import {conf, language} from "../../../assets/monaco/languages/mysql/mysql";
import {DatabaseService} from "../../services/database.service";
import {GlobalErrorHandler} from "../../services/error-handler.service";
import {Utils} from "../../shared/util/utils";
import {Row} from "../../models/rest/rest-models";
import {PageHeaderService} from "../../shared/modules/page-header/page-header.service";

@Component({
    selector: 'app-custom',
    templateUrl: './custom.component.html',
    styleUrls: ['./custom.component.scss']
})
export class CustomComponent implements OnInit
{
    editor;
    editorOptions = {
        theme: 'mysqlTheme',
        language: 'mysql',
        autocomplete: true,
        automaticLayout: true,
        minimap: {enabled: false},
        scrollBeyondLastLine: false,
        fontSize: 16,
        formatOnPaste: true};
    code: string= 'SELECT * FROM information_schema.tables;\n';
    results: Row[] = [];

    pageHeaderPath = [];

    constructor(private databaseService: DatabaseService,
                private errorHandler: GlobalErrorHandler,
                private pageHeaderService: PageHeaderService)
    {
    }

    ngOnInit()
    {
        this.pageHeaderPath = this.pageHeaderService.getPathFromID('custom');
    }

    monacoInit(editor)
    {
        this.editor = editor;
    }

    execute()
    {
        this.databaseService.executeCustomSql(this.editor.getValue())
            .then(results => this.results = results)
            .catch(error => this.errorHandler.handleError(error));
    }

    executeSelected()
    {
        this.databaseService.executeCustomSql(this.editor.getModel().getValueInRange(this.editor.getSelection()))
            .then(results => this.results = results)
            .catch(error => this.errorHandler.handleError(error));
    }

}
