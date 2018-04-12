import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule} from "@angular/forms";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { ConfirmdialogComponent } from './components/confirmdialog/confirmdialog.component';
import { NotificationComponent } from './components/notification/notification.component';
import { NewDbComponent } from './components/db-dialog/new-db/new-db.component';
import { DropDbComponent } from './components/db-dialog/drop-db/drop-db.component';

@NgModule({
    imports: [
        CommonModule,
        LayoutRoutingModule,
        FormsModule,
        NgbModule
    ],
    declarations: [LayoutComponent, SidebarComponent, HeaderComponent, ConfirmdialogComponent, NotificationComponent, NewDbComponent, DropDbComponent],
    entryComponents: [ConfirmdialogComponent, NewDbComponent, DropDbComponent]
})
export class LayoutModule {}
