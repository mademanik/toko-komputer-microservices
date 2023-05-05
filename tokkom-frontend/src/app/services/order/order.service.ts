import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  constructor(private _httpClient: HttpClient) {}

  private baseUrl = 'http://localhost:8080';

  createOrder(data: any) {
    return this._httpClient.post<any>(`${this.baseUrl}/tokkom/api/order`, data);
  }
}
