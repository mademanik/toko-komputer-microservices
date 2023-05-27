import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProductService } from 'src/app/services/product/product.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.scss'],
})
export class DetailProductComponent implements OnInit {
  baseUrl = environment.baseUrl;
  row: any;
  thumbnail: string = '';
  images: string[] = [];
  downloadUrl: string = `${this.baseUrl}/tokkom/api/product/download`;

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { id: any },
    private productService: ProductService,
    public dialogRef: MatDialogRef<DetailProductComponent>
  ) {}

  ngOnInit(): void {
    this.getProductById(this.data.id);
  }

  getProductById(id: String) {
    this.productService.getProductById(id).subscribe({
      next: (res) => {
        this.row = res;
        this.thumbnail = `${this.downloadUrl}/${res.id}/${res.thumbnail}`;
        this.images.push(this.thumbnail);
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

  setThumbnail(url: string) {
    this.thumbnail = url;
  }
}
