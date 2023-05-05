import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { BreakpointObserver } from '@angular/cdk/layout';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/services/product/product.service';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.scss'],
})
export class PanelComponent implements OnInit {
  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  isMobile: boolean = false;

  constructor(
    private observer: BreakpointObserver,
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.getAllProducts();
  }

  getAllProducts() {
    this.productService.getDataProducts().subscribe({
      next: (res) => {
        console.log(res);
      },
      error: (err) => {
        alert(err);
      },
    });
  }

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
