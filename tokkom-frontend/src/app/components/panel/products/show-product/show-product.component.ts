import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProductService } from 'src/app/services/product/product.service';

@Component({
  selector: 'app-show-product',
  templateUrl: './show-product.component.html',
  styleUrls: ['./show-product.component.scss'],
})
export class ShowProductComponent implements OnInit {
  row: any;
  thumbnail: string = '';
  images: string[] = [];
  downloadUrl: string = 'http://localhost:8080/tokkom/api/product/download';

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { id: any },
    private productService: ProductService,
    private dialogRef: MatDialogRef<ShowProductComponent>
  ) {}

  ngOnInit(): void {
    this.getProductById(this.data.id);
  }

  getProductById(id: String) {
    this.productService.getProductById(id).subscribe({
      next: (res) => {
        this.row = res;
        this.thumbnail = `${this.downloadUrl}/${res.id}/${res.thumbnail}`;
        if (res.images) {
          for (let i = 0; i < res.images.length; i++) {
            this.images.push(`${this.downloadUrl}/${res.id}/${res.images[i]}`);
          }
        }
      },
      error: (err) => {
        alert(err);
      },
    });
  }
}
