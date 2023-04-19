import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { BreakpointObserver } from '@angular/cdk/layout';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.scss'],
})
export class PanelComponent {
  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  isMobile: boolean = false;

  constructor(private observer: BreakpointObserver) {}

  ngAfterViewInit() {
    this.observer.observe(['(max-width: 800px)']).subscribe((res) => {
      if (res.matches) {
        this.sidenav.mode = 'over';
        this.sidenav.close();
        this.isMobile = true;
      } else {
        this.sidenav.mode = 'side';
        this.sidenav.open();
        this.isMobile = false;
      }
    });
  }

  sidenavToggle() {
    this.sidenav.toggle();
  }
}
