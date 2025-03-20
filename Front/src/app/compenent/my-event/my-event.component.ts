import { Component } from '@angular/core';
import { EventService } from '../../services/EventService';
import { Event } from '../../model/event.model';
import { ReactiveFormsModule } from '@angular/forms';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
@Component({
  selector: 'app-my-event',
  standalone: true,
  imports: [ReactiveFormsModule, SidebarComponent, CommonModule,],
  templateUrl: './my-event.component.html',
  styleUrl: './my-event.component.scss'
})
export class MyEventComponent {

  events: Event[] = [];

  constructor(private eventService: EventService,private router: Router) { }

  ngOnInit(): void {
    this.loadUserEvents();
  }

  loadUserEvents(): void {
      this.eventService.getEventsByUserEmail().subscribe((events: any) => {
        this.events = events;
      });

  }
  

  editEvent(eventId: number): void {
    this.router.navigate([`/edit-event/${eventId}`]);
  }

  deleteEvent(eventId: number): void {
    this.eventService.deleteEvent(eventId).subscribe(() => {
      alert('Event deleted successfully!');
      this.loadUserEvents();
    });
  }
}
