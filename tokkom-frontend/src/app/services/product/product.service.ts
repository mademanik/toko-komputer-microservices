import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from 'src/app/models/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  constructor(private _httpClient: HttpClient) {}

  private baseUrl = 'http://localhost:8080';

  createProduct(formData: FormData) {
    const req = new HttpRequest(
      'POST',
      `${this.baseUrl}/tokkom/api/product/`,
      formData,
      {
        reportProgress: true,
        responseType: 'json',
      }
    );

    return this._httpClient.request(req);
  }

  getDataProducts(): Observable<Product[]> {
    return this._httpClient.get<Product[]>(
      `${this.baseUrl}/tokkom/api/product/`
    );
  }

  getProductById(id: any): Observable<Product> {
    return this._httpClient.get<Product>(
      `${this.baseUrl}/tokkom/api/product/${id}`
    );
  }

  deleteProductById(id: any): Observable<any> {
    return this._httpClient.delete(`${this.baseUrl}/tokkom/api/product/${id}`);
  }

  updateProduct(id: String, formData: FormData) {
    const req = new HttpRequest(
      'PUT',
      `${this.baseUrl}/tokkom/api/product/${id}`,
      formData,
      {
        reportProgress: true,
        responseType: 'json',
      }
    );

    return this._httpClient.request(req);
  }

  downloadFile(id: String, fileName: String) {
    return this._httpClient.get<any>(
      `${this.baseUrl}/tokkom/api/product/download/${id}/${fileName}`
    );
  }
}
