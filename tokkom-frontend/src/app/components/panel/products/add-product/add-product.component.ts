import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
} from '@angular/forms';
import { ProductService } from 'src/app/services/product/product.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.scss'],
})
export class AddProductComponent {
  productForm: FormGroup = this.formBuilder.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    price: ['', Validators.required],
    stock: ['', Validators.required],
    brand: ['', Validators.required],
    category: ['', Validators.required],
    thumbnail: ['', Validators.required],
    images: ['', Validators.required],
  });

  // single image
  currentFile?: File;
  fileName = 'Select Thumbnail';
  singlePreview = '';

  // multi image
  selectedFiles?: FileList;
  selectedFileNames: string[] = [];

  multiPreview: string[] = [];

  singleFile: string = '';
  multiFiles: string[] = [];
  constructor(
    private formBuilder: FormBuilder,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
    public dialogRef: MatDialogRef<AddProductComponent>
  ) {}

  // select single image
  selectFile(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file: File = event.target.files[0];
      this.currentFile = file;
      this.fileName = this.currentFile.name;

      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.singlePreview = e.target.result;
      };

      reader.readAsDataURL(this.currentFile);
      this.singleFile = event.target.files[0];
    } else {
      this.fileName = 'Select Thumbnail';
    }
  }

  // select multiple images
  selectFiles(event: any): void {
    this.selectedFileNames = [];
    this.selectedFiles = event.target.files;

    this.multiPreview = [];
    if (this.selectedFiles && this.selectedFiles[0]) {
      const numberOfFiles = this.selectedFiles.length;
      for (let i = 0; i < numberOfFiles; i++) {
        const reader = new FileReader();

        reader.onload = (e: any) => {
          this.multiPreview.push(e.target.result);
        };

        reader.readAsDataURL(this.selectedFiles[i]);

        this.selectedFileNames.push(this.selectedFiles[i].name);
        this.multiFiles.push(event.target.files[i]);
      }
    }
  }

  submit(): void {
    if (this.productForm.valid) {
      const getTitle = this.productForm.get('title')?.value;
      const getDesc = this.productForm.get('description')?.value;
      const getPrice = this.productForm.get('price')?.value;
      const getStock = this.productForm.get('stock')?.value;
      const getBrand = this.productForm.get('brand')?.value;
      const getCategory = this.productForm.get('category')?.value;

      const formData = new FormData();

      const jsonData = {
        title: getTitle,
        description: getDesc,
        price: getPrice,
        stock: getStock,
        brand: getBrand,
        category: getCategory,
      };

      //append product
      formData.append('product', JSON.stringify(jsonData));

      //append single images
      formData.append('thumbnail', this.singleFile);

      //append multi images
      for (var i = 0; i < this.multiFiles.length; i++) {
        formData.append('images', this.multiFiles[i]);
      }

      this.productService.createProduct(formData).subscribe({
        next: (res) => {
          // console.log(res);
          this.dialogRef.close({
            message: 'success',
          });
        },
        error: (err) => {
          this.dialogRef.close({
            message: 'error',
          });
        },
      });
    } else {
      this.dialogRef.close({
        message: 'invalid',
      });
    }
  }
}
