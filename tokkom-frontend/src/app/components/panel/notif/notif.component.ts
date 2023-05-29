import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { Notification } from 'src/app/models/notification.model';
import { NotificationService } from 'src/app/services/notification/notification.service';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'app-notif',
  templateUrl: './notif.component.html',
  styleUrls: ['./notif.component.scss'],
})
export class NotifComponent implements OnInit {
  @Output() sendEventToParent = new EventEmitter();

  constructor(
    private notificationService: NotificationService,
    private router: Router
  ) {}

  notifBadge: number = 0;
  activeNotif: Notification[] = [];

  ngOnInit(): void {
    this.getNotif();
  }

  setExpired(id: any): void {
    const formData = new FormData();

    const jsonData = {
      id: id,
      isExpired: '1',
    };

    formData.append('notifExpire', JSON.stringify(jsonData));

    this.notificationService.setExpiredNotification(formData).subscribe({
      next: (res) => {
        this.getNotif();

        this.sendEventToParent.emit({ menu: 'orders' });
      },
      error: (err) => {
        console.log(err);
      },
    });
    this.router.navigate(['/panel/orders']);
  }

  getNotif() {
    this.notificationService
      .getNotifications()
      .pipe(
        map((notif: any[]) => notif.filter((not: any) => not.isExpired === 0))
      )
      .subscribe({
        next: (res) => {
          this.activeNotif = res;
          this.notifBadge = this.activeNotif.length;
        },
        error: (err) => {
          alert(err);
        },
      });
  }
}
