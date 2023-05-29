import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private _httpClient: HttpClient) {}

  private baseUrl = environment.baseUrl;

  getNotifications() {
    return this._httpClient.get<any>(`${this.baseUrl}/tokkom/api/notification`);
  }

  setExpiredNotification(formData: FormData) {
    const req = new HttpRequest(
      'POST',
      `${this.baseUrl}/tokkom/api/notification/setExpired`,
      formData,
      {
        reportProgress: true,
        responseType: 'json',
      }
    );

    return this._httpClient.request(req);
  }
}
