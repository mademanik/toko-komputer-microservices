import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  constructor(private _httpClient: HttpClient) {}

  private baseUrl = "http://localhost:8080";

  // getDataBlogs() {
  //   return this._httpClient.get<any>(`${this.baseUrl}/api/blogs`);
  // }

  createProduct(formData: FormData) {
    const req = new HttpRequest("POST", `${this.baseUrl}/tokkom/api/product/`, formData, {
      reportProgress: true,
      responseType: "json",
    });

    return this._httpClient.request(req);
  }

  getDataProducts() {
    return this._httpClient.get<any>(`${this.baseUrl}/tokkom/api/product/`);
  }

  getProductById(id: String) {
    return this._httpClient.get<any>(
      `${this.baseUrl}/tokkom/api/product/${id}`
    );
  }

  getProductContainsTitle(title: String) {
    return this._httpClient.get<any>(
      `${this.baseUrl}/tokkom/api/product?title=${title}`
    );
  }

  getProductContainsCategory(category: String) {
    return this._httpClient.get<any>(
      `${this.baseUrl}/tokkom/api/product?category=${category}`
    );
  }

  deleteProductById(id: String) {
    return this._httpClient.delete<any>(
      `${this.baseUrl}:8080/tokkom/api/product/${id}`
    );
  }

  updateProduct(id: String, formData: FormData) {
    const req = new HttpRequest("PUT", `${this.baseUrl}/tokkom/api/product/${id}`, formData, {
      reportProgress: true,
      responseType: "json",
    });

    return this._httpClient.request(req);
  }

  downloadFile(id: String, fileName: String) {
    return this._httpClient.get<any>(
      `${this.baseUrl}/tokkom/api/product/download/${id}/${fileName}`
    );
  }
}
