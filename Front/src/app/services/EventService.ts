import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event } from '../model/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private apiUrl = `http://localhost:8080/api/events`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token'); 
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}` 
    });
  }

  createEvent(event: Event): Observable<Event> {
    return this.http.post<Event>(`${this.apiUrl}/create`, event, {
      headers: this.getAuthHeaders()
    });
  }
  

  
  getAllEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/list`, { headers: this.getAuthHeaders() });
  }

  getEventsByUserEmail(): Observable<any> {
    return this.http.get(`${this.apiUrl}/myevents`, {
      headers: this.getAuthHeaders()  
    });
  }
  


    deleteEvent(eventId: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${eventId}`, {
          headers: this.getAuthHeaders()  
        });
      }
    
      editEvent(eventId: number, updatedEvent: Event): Observable<Event> {
        return this.http.put<Event>(`${this.apiUrl}/update/${eventId}`, updatedEvent, {
          headers: this.getAuthHeaders(),
          withCredentials: true  
        });
        
      }
      

  getEventById(id: number): Observable<Event> {
    return this.http.get<Event>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  registerForEvent(eventId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${eventId}/register`, {}, {
      headers: this.getAuthHeaders(),
    });
  }

  
  unregisterFromEvent(eventId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${eventId}/unregister`, {}, {
      headers: this.getAuthHeaders(),
    });
  }
  getRegisteredEvents(): Observable<any> {
    return this.http.get(`${this.apiUrl}/registredEvents`, { headers: this.getAuthHeaders() });
  }
  

}
