import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ProductService } from 'src/app/services/product/product.service';
import { OrderService } from 'src/app/services/order/order.service';
import { NotificationService } from 'src/app/services/notification/notification.service';
import { Product } from 'src/app/models/product.model';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements AfterViewInit, OnInit {
  displayedColumns: string[] = ['title', 'brand', 'category', 'stock', 'price'];

  productSummary: number = 0;
  orderSummary: number = 0;
  notifSummary: number = 0;

  dataSource = new MatTableDataSource<Product>();
  ELEMENT_DATA = [];

  @ViewChild(MatPaginator) paginator: any = MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(
    private productService: ProductService,
    private orderService: OrderService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.getOrders();
    this.getProducts();
    this.getNotifs();
  }

  getProducts() {
    this.productService.getDataProducts().subscribe({
      next: (res) => {
        this.dataSource.data = res;
        this.productSummary = res.length;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getOrders() {
    this.orderService.getAllOrders().subscribe({
      next: (res) => {
        this.orderSummary = res.length;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getNotifs() {
    this.notificationService
      .getNotifications()
      .pipe(
        map((notif: any[]) => notif.filter((not: any) => not.isExpired === 0))
      )
      .subscribe({
        next: (res) => {
          this.notifSummary = res.length;
        },
        error: (err) => {
          console.log(err);
        },
      });
  }
}
