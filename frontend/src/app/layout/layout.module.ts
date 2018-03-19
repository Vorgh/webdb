import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header.component';
import { ConfirmdialogComponent } from './components/confirmdialog/confirmdialog.component';
import { NotificationComponent } from './components/notification/notification.component';

@NgModule({
    imports: [
        CommonModule,
        LayoutRoutingModule,
        NgbModule
    ],
    declarations: [LayoutComponent, SidebarComponent, HeaderComponent, ConfirmdialogComponent, NotificationComponent],
    entryComponents: [ConfirmdialogComponent]
})
export class LayoutModule {}
