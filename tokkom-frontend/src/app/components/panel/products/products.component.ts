import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { AddProductComponent } from './add-product/add-product.component';
import { MatTableDataSource } from '@angular/material/table';
import { ProductService } from 'src/app/services/product/product.service';
import { ShowProductComponent } from './show-product/show-product.component';
import { DeleteProductComponent } from './delete-product/delete-product.component';
import { EditProductComponent } from './edit-product/edit-product.component';
import { MatSort } from '@angular/material/sort';
import { Product } from 'src/app/models/product.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements AfterViewInit, OnInit {
  displayedColumns: string[] = [
    'title',
    'brand',
    'category',
    'stock',
    'price',
    'action',
  ];

  dataSource = new MatTableDataSource<Product>();
  ELEMENT_DATA = [];

  @ViewChild(MatPaginator) paginator: any = MatPaginator;
  @ViewChild(MatSort) sort: any = MatSort;

  constructor(
    public dialog: MatDialog,
    private productService: ProductService,
    private _snackBar: MatSnackBar
  ) {}

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
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
    this.getProducts();
  }

  getProducts() {
    this.productService.getDataProducts().subscribe({
      next: (res) => {
        this.dataSource.data = res;
      },
      error: (err) => {
        alert(err);
      },
    });
  }

  addDialog() {
    const dialogRef = this.dialog.open(AddProductComponent, {
      width: '50%',
      position: { top: '20px' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      setTimeout(() => {
        this.getProducts();
      }, 500);

      if (result?.message == 'success') {
        this.openSnackbarSuccess('Success', 'Product successfully created');
      } else if (result?.message == 'error') {
        this.openSnackbarError('Error', 'Product create Failed');
      } else if (result?.message == 'invalid') {
        this.openSnackbarError('Error', 'Form invalid');
      }
    });
  }

  showDialog(id: String) {
    const dialogRef = this.dialog.open(ShowProductComponent, {
      width: '50%',
      position: { top: '20px' },
      data: { id: id },
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.getProducts();
    });
  }

  editDialog(id: String) {
    const dialogRef = this.dialog.open(EditProductComponent, {
      width: '50%',
      position: { top: '20px' },
      data: { id: id },
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.getProducts();

      if (result?.message == 'success') {
        this.openSnackbarSuccess('Success', 'Product successfully updated');
      } else if (result?.message == 'error') {
        this.openSnackbarError('Error', 'Product create Failed');
      } else if (result?.message == 'invalid') {
        this.openSnackbarError('Error', 'Form invalid');
      }
    });
  }

  deleteDialog(id: String) {
    const dialogRef = this.dialog.open(DeleteProductComponent, {
      width: '20%',
      position: { top: '20px' },
      data: { id: id },
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.getProducts();

      if (result?.message == 'success') {
        this.openSnackbarSuccess('Success', 'Product deleted successfully');
      } else if (result?.message == 'error') {
        this.openSnackbarError('Error', 'Product create Failed');
      } else if (result?.message == 'invalid') {
        this.openSnackbarError('Error', 'Form invalid');
      }
    });
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
