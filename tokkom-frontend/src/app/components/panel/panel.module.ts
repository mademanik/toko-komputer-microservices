import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

// Modules
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatRippleModule } from '@angular/material/core';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';

// Components
import { PanelComponent } from './panel.component';
import { ProfileComponent } from './profile/profile.component';
import { HomeComponent } from '../pages/home/home.component';
import { AboutComponent } from '../pages/about/about.component';
import { HelpComponent } from '../pages/help/help.component';

const routes: Routes = [
  {
    path: 'panel',
    component: PanelComponent,
    children: [
      { path: '', component: HomeComponent },
      { path: 'home', component: HomeComponent },
      { path: 'about', component: AboutComponent },
      { path: 'help', component: HelpComponent },
    ],
  },
];

@NgModule({
  declarations: [PanelComponent, ProfileComponent],
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatRippleModule,
    MatMenuModule,
    MatSidenavModule,
    MatDividerModule,
    RouterModule.forChild(routes),
  ],
})
export class PanelModule {}
