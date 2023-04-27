import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { AddEventListenerOptions } from 'rxjs/internal/observable/fromEvent';
import { AddProductComponent } from './add-product/add-product.component';
import { MatTableDataSource } from '@angular/material/table';

export interface PeriodicElement {
  no: number;
  title: string;
  brand: string;
  category: string;
  action: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  {
    no: 1,
    title: 'Robot Headset G10',
    brand: 'Robot',
    category: 'Headset',
    action: '',
  },
  {
    no: 2,
    title: 'Infinix Smartphone',
    brand: 'Infinix',
    category: 'Smartphone',
    action: '',
  },
  {
    no: 3,
    title: 'Asus TUF Gming',
    brand: 'Asus',
    category: 'Laptop',
    action: '',
  },
];

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements AfterViewInit {
  displayedColumns: string[] = ['no', 'title', 'brand', 'category','action'];
  dataSource = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);

  @ViewChild(MatPaginator) paginator: any = MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(public dialog: MatDialog) {}

  openDialog() {
    const dialogRef = this.dialog.open(AddProductComponent, {
      width: '50%',
      position: { top: '20px' },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
