import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProductService } from 'src/app/services/product/product.service';
import {
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['./edit-product.component.scss'],
})
export class EditProductComponent implements OnInit {
  baseUrl = environment.baseUrl;
  productForm: FormGroup = this.formBuilder.group({
    id: ['', Validators.required],
    title: ['', Validators.required],
    description: ['', Validators.required],
    price: ['', Validators.required],
    stock: ['', Validators.required],
    brand: ['', Validators.required],
    category: ['', Validators.required],
    thumbnail: [''],
    images: [''],
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

  downloadUrl: string = `${this.baseUrl}/tokkom/api/product/download`;

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { id: any },
    private formBuilder: FormBuilder,
    private productService: ProductService,
    private dialogRef: MatDialogRef<EditProductComponent>
  ) {}

  ngOnInit(): void {
    this.getProductById(this.data.id);
  }

  getProductById(id: String) {
    this.productService.getProductById(id).subscribe({
      next: (res) => {
        const { id, title, description, price, stock, brand, category } = res;

        this.productForm.setValue({
          id: id,
          title: title,
          description: description,
          price: price,
          stock: stock,
          brand: brand,
          category: category,
          thumbnail: '',
          images: '',
        });

        this.singlePreview = `${this.downloadUrl}/${res.id}/${res.thumbnail}`;
        if (res.images) {
          for (let i = 0; i < res.images.length; i++) {
            this.multiPreview.push(
              `${this.downloadUrl}/${res.id}/${res.images[i]}`
            );
          }
        }
      },
      error: (err) => {
        alert(err);
      },
    });
  }

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
      const getId = this.productForm.get('id')?.value;
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
      if (this.multiFiles.length === 0) {
        //if multi images null then append empty string
        formData.append('images', '');
      } else {
        for (var i = 0; i < this.multiFiles.length; i++) {
          formData.append('images', this.multiFiles[i]);
        }
      }

      this.productService.updateProduct(getId, formData).subscribe({
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
    } else {
      this.dialogRef.close({
        message: 'invalid',
      });
    }
  }
}
