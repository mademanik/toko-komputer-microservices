import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { AddProductComponent } from '../../panel/products/add-product/add-product.component';
import { MatTableDataSource } from '@angular/material/table';
import { ProductService } from 'src/app/services/product/product.service';
import { Product } from 'src/app/models/product.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements AfterViewInit, OnInit {
  displayedColumns: string[] = ['title', 'brand', 'category', 'stock', 'price'];

  dataSource = new MatTableDataSource<Product>();
  ELEMENT_DATA = [];

  @ViewChild(MatPaginator) paginator: any = MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  constructor(private productService: ProductService) {}

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
}
