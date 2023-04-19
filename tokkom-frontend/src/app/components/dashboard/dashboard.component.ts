import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { BreakpointObserver } from '@angular/cdk/layout';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  @ViewChild(MatSidenav)
  sidelanding!: MatSidenav;

  isMobile: boolean = false;

  constructor(private observer: BreakpointObserver) {}
  ngOnInit(): void {}

  ngAfterViewInit() {
    this.observer.observe(['(max-width: 800px)']).subscribe((res) => {
      if (res.matches) {
        this.sidelanding.mode = 'over';
        this.isMobile = true;
      } else {
        this.sidelanding.mode = 'side';
        this.isMobile = false;
      }
    });
  }

  sideToggle() {
    this.sidelanding.toggle();
  }
}
