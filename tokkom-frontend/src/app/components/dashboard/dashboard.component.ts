import { Component, OnInit, ViewChild } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  isMobile: boolean = false;
  menuOpen: boolean = false;

  constructor(private observer: BreakpointObserver) {}
  ngOnInit() {
    console.log(this.menuOpen);
  }

  ngAfterViewInit() {
    this.observer.observe(['(max-width: 800px)']).subscribe((res) => {
      if (res.matches) {
        this.isMobile = true;
      } else {
        this.isMobile = false;
      }
    });
  }
}
