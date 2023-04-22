import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { HttpClientModule } from '@angular/common/http';
import { PanelModule } from './components/panel/panel.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatRippleModule } from '@angular/material/core';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatListModule } from '@angular/material/list';
import { MatInputModule } from '@angular/material/input';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { JumbotonComponent } from './components/dashboard/jumboton/jumboton.component';
import { ProductsComponent } from './components/dashboard/products/products.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { NavbarComponent } from './components/dashboard/navbar/navbar.component';
import { FooterComponent } from './components/dashboard/footer/footer.component';

import {MatDialogModule} from '@angular/material/dialog';
import { FormProductComponent } from './components/dashboard/products/form-product/form-product.component';
import {MatFormFieldModule} from '@angular/material/form-field';



@NgModule({
  declarations: [AppComponent, DashboardComponent, JumbotonComponent, ProductsComponent, NavbarComponent, FooterComponent, FormProductComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    PanelModule,
    MatToolbarModule,
    MatSidenavModule,
    MatButtonModule,
    MatIconModule,
    MatRippleModule,
    MatMenuModule,
    MatDividerModule,
    MatCardModule,
    MatChipsModule,
    MatListModule,
    MatInputModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    FlexLayoutModule,
    MatDialogModule,
    MatFormFieldModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
