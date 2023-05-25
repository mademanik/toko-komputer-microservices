import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'src/app/services/product/product.service';

@Component({
  selector: 'app-delete-product',
  templateUrl: './delete-product.component.html',
  styleUrls: ['./delete-product.component.scss'],
})
export class DeleteProductComponent {
  id: String = '';
  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { id: any },
    private productService: ProductService,
    private dialogRef: MatDialogRef<DeleteProductComponent>,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  deleteProduct() {
    this.productService.deleteProductById(this.data.id).subscribe({
      next: (res) => {
        this.dialogRef.close({
          message: 'success',
        });
      },
      error: (err) => {
        this.dialogRef.close({
          message: 'error',
        });
        console.log(err);
      },
    });
  }
}
