import { Component, OnInit, Input } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormProductComponent } from './form-product/form-product.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/services/product/product.service';
import {
  MatSnackBar,
  MatSnackBarHorizontalPosition,
  MatSnackBarVerticalPosition,
} from '@angular/material/snack-bar';
import { Product } from 'src/app/models/product.model';
import { environment } from 'src/environments/environment';
import { DetailProductComponent } from './detail-product/detail-product.component';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  buyProduct: string = 'Buy Product';
  rows: Product[] = [];
  baseUrl = environment.baseUrl;
  downloadUrl: string = `${this.baseUrl}/tokkom/api/product/download`;

  constructor(
    public dialog: MatDialog,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
    private _snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts() {
    this.productService.getDataProducts().subscribe({
      next: (res) => {
        this.rows = res;
      },
      error: (err) => {
        alert(err);
      },
    });
  }

  orderDialog(id: any) {
    const dialogRef = this.dialog.open(FormProductComponent, {
      width: '50%',
      position: { top: '20px' },
      data: { id: id },
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.getProducts();

      if (result?.message == 'success') {
        this.openSnackbarSuccess('Success', 'Product successfully created');
      } else if (result?.message == 'error') {
        this.openSnackbarError('Error', 'Product create Failed');
      } else if (result?.message == 'invalid') {
        this.openSnackbarError('Error', 'Form invalid');
      }
    });
  }

  detailDialog(id: any) {
    const dialogRef = this.dialog.open(DetailProductComponent, {
      width: '50%',
      position: { top: '20px' },
      maxHeight: '90vh',
      data: { id: id },
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.getProducts();

      if (result?.message == 'success') {
        this.openSnackbarSuccess('Success', 'Order successfully created');
      } else if (result?.message == 'error') {
        this.openSnackbarError('Error', 'Order create Failed');
      } else if (result?.message == 'invalid') {
        this.openSnackbarError('Error', 'Form invalid');
      }
    });
  }

  truncateTitle(title: any, length: number) {
    if (title.length > length) {
      return title.substring(0, length) + '...';
    }
    return title;
  }

  openSnackbarSuccess(title: string, message: string) {
    this._snackBar.open(message, title, {
      horizontalPosition: 'right',
      verticalPosition: 'top',
      duration: 3000,
      panelClass: 'app-notification-success',
    });
  }

  openSnackbarError(title: string, message: string) {
    this._snackBar.open(message, title, {
      horizontalPosition: 'right',
      verticalPosition: 'top',
      duration: 3000,
      panelClass: 'app-notification-error',
    });
  }
}
