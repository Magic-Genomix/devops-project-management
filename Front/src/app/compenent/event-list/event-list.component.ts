import { Component, OnInit } from '@angular/core';
import { EventService } from '../../services/EventService';
import { Event } from '../../model/event.model';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';  
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-event-list',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, SidebarComponent, CommonModule],  
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  events: Event[] = [];
  filteredEvents: Event[] = [];
  filterType = 'all';
  registeredEvents: number[] = [];

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
    this.loadRegisteredEvents();
  }

  loadEvents(): void {
    this.eventService.getAllEvents().subscribe((events: Event[]) => {
      this.events = events;
      this.applyFilter();
    });
  }

  loadRegisteredEvents(): void {
    this.eventService.getRegisteredEvents().subscribe((eventIds: number[]) => {
      this.registeredEvents = eventIds;
      this.applyFilter();
    });
  }

  applyFilter(): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Normaliser la date pour éviter les problèmes de fuseau horaire
  
    let upcomingEvents = this.events.filter(event => {
      const eventDate = new Date(event.date);
      eventDate.setHours(0, 0, 0, 0);
      return eventDate >= today;
    });
  
    if (this.filterType === 'all') {
      this.filteredEvents = upcomingEvents;
    } else if (this.filterType === 'registered') {
      this.filteredEvents = upcomingEvents.filter(event => this.registeredEvents.includes(event.id));
    } else if (this.filterType === 'unregistered') {
      this.filteredEvents = upcomingEvents.filter(event => !this.registeredEvents.includes(event.id));
    }
  }
  
  registerForEvent(eventId: number) {
    this.eventService.registerForEvent(eventId).subscribe( {
    });
    alert('You have registered for this event!');
    this.loadRegisteredEvents();
  }

  unregisterFromEvent(eventId: number) {
    this.eventService.unregisterFromEvent(eventId).subscribe( {
    });
    alert('You have unregistered from this event!');
    this.loadRegisteredEvents();
  }


  isRegistered(eventId: number): boolean {
    return this.registeredEvents.includes(eventId);
  }
}
