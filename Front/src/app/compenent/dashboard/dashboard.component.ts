import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { EventService } from '../../services/EventService';
import { AuthService } from '../../services/auth/auth.service';
import { Event } from '../../model/event.model';  // Assurez-vous que le modèle Event est importé
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SidebarComponent,CommonModule,ReactiveFormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  events: Event[] = [];
  closestEvent: Event | null = null;
  createdEventsCount: number = 0;

  constructor(private eventService: EventService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadEvents();
    this.loadUserEvents();
  }

  loadEvents(): void {
    this.eventService.getAllEvents().subscribe((events: Event[]) => {
      this.events = events;

      // Vérifier et filtrer les événements pour obtenir le plus proche
      const today = new Date();

      // Filtrer les événements dont la date est supérieure ou égale à aujourd'hui
      this.closestEvent = this.events
        .filter(event => {
          const eventDate = new Date(event.date);
          return eventDate >= today; // Comparaison de la date
        })
        .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())[0] || null;

      console.log(this.closestEvent);
    });
  }

  loadUserEvents(): void {
    this.eventService.getEventsByUserEmail().subscribe((events: any) => {
      this.events = events;
      this.createdEventsCount = events.length;
    });

}

}
