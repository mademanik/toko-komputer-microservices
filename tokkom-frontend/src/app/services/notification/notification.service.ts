import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private _httpClient: HttpClient) {}

  private baseUrl = 'http://localhost:8080';

  getNotifications() {
    return this._httpClient.get<any>(`${this.baseUrl}/tokkom/api/notification`);
  }

  setExpiredNotification(data: any) {
    return this._httpClient.post<any>(`${this.baseUrl}/tokkom/api/notification/setExpired`, data);
  }
}
