import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSort, MatSortable } from '@angular/material/sort';
import { OrderService } from 'src/app/services/order/order.service';
import { Order } from 'src/app/models/order.model';
import { ShowOrderComponent } from './show-order/show-order.component';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss'],
})
export class OrdersComponent implements AfterViewInit, OnInit {
  displayedColumns: string[] = [
    'productTitle',
    'customerName',
    'customerAddress',
    'customerPhoneNumber',
    'productQty',
    'action',
  ];
  dataSource = new MatTableDataSource<Order>();
  ELEMENT_DATA = [];

  @ViewChild(MatPaginator) paginator: any = MatPaginator;
  @ViewChild(MatSort) sort: any = MatSort;

  constructor(
    public dialog: MatDialog,
    private orderService: OrderService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;

    // script di bawah untuk initial sorting
    // this.sort.sort({ id: 'productQty', start: 'desc' } as MatSortable);
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  ngOnInit(): void {
    this.getOrders();
  }

  getOrders() {
    this.orderService.getAllOrders().subscribe({
      next: (res) => {
        this.dataSource.data = res;
      },
      error: (err) => {
        alert(err);
      },
    });
  }

  showDialog(id: String) {
    const dialogRef = this.dialog.open(ShowOrderComponent, {
      width: '50%',
      position: { top: '20px' },
      data: { id: id },
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.getOrders();
    });
  }
}
