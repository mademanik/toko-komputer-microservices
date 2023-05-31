import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ProductService } from 'src/app/services/product/product.service';
import { OrderService } from 'src/app/services/order/order.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-form-product',
  templateUrl: './form-product.component.html',
  styleUrls: ['./form-product.component.scss'],
})
export class FormProductComponent implements OnInit {
  orderForm: FormGroup = this.formBuilder.group({
    productId: ['', Validators.required],
    productTitle: ['', Validators.required],
    productQty: ['', Validators.required],
    customerName: ['', Validators.required],
    customerAddress: ['', Validators.required],
    customerPhoneNumber: ['', Validators.required],
    description: [''],
  });

  baseUrl = environment.baseUrl;

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { id: any },
    private productService: ProductService,
    private orderService: OrderService,
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<FormProductComponent>
  ) {}

  ngOnInit(): void {
    this.getProductById(this.data.id);
  }

  getProductById(id: String) {
    this.productService.getProductById(id).subscribe({
      next: (res) => {
        const { id, title } = res;

        this.orderForm.setValue({
          productId: id,
          productTitle: title,
          productQty: '',
          customerName: '',
          customerAddress: '',
          customerPhoneNumber: '',
          description: '',
        });
      },
      error: (err) => {
        alert(err);
      },
    });
  }

  submit(): void {
    if (this.orderForm.valid) {
      const getProductId = this.orderForm.get('productId')?.value;
      const getProductTitle = this.orderForm.get('productTitle')?.value;
      const getProductQty = this.orderForm.get('productQty')?.value;
      const getCustomerName = this.orderForm.get('customerName')?.value;
      const getCustomerAddress = this.orderForm.get('customerAddress')?.value;
      const getCustomerPhoneNumber = this.orderForm.get(
        'customerPhoneNumber'
      )?.value;
      const getDescription = this.orderForm.get('description')?.value;

      const formData = new FormData();

      const jsonData = {
        productId: getProductId,
        productTitle: getProductTitle,
        productQty: getProductQty,
        customerName: getCustomerName,
        customerAddress: getCustomerAddress,
        customerPhoneNumber: getCustomerPhoneNumber,
        description: getDescription,
      };

      formData.append('order', JSON.stringify(jsonData));

      this.orderService.createOrder(getProductId, formData).subscribe({
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
