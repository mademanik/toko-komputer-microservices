import { Component, OnInit, Input } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormProductComponent } from './form-product/form-product.component';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  constructor(public dialog: MatDialog) {}
  ngOnInit(): void {}

  openDialog() {
    const dialogRef = this.dialog.open(FormProductComponent, {
      width: '50%',
      position: { top: '20px' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
