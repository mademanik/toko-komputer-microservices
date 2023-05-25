import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpRequest,
  HttpEvent,
  HttpHeaders,
} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  constructor(private _httpClient: HttpClient) {}

  private baseUrl = environment.baseUrl;

  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  createOrder(id: any, formData: FormData) {
    const req = new HttpRequest(
      'POST',
      `${this.baseUrl}/tokkom/api/order/${id}`,
      formData,
      {
        reportProgress: true,
        responseType: 'json',
      }
    );

    return this._httpClient.request(req);
  }
}
