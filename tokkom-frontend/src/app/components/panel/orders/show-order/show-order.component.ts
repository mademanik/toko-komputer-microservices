import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { OrderService } from 'src/app/services/order/order.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-show-order',
  templateUrl: './show-order.component.html',
  styleUrls: ['./show-order.component.scss'],
})
export class ShowOrderComponent implements OnInit {
  baseUrl = environment.baseUrl;
  row: any;

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { id: any },
    private orderService: OrderService,
    private dialogRef: MatDialogRef<ShowOrderComponent>
  ) {}

  ngOnInit(): void {
    this.getOrderById(this.data.id);
  }

  getOrderById(id: String) {
    this.orderService.getOrderById(id).subscribe({
      next: (res) => {
        this.row = res;
      },
      error: (err) => {
        alert(err);
      },
    });
  }
}
