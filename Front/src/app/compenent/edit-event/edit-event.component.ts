import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../services/EventService';
import { Event } from '../../model/event.model';
import { ReactiveFormsModule } from '@angular/forms';

import { SidebarComponent } from '../sidebar/sidebar.component';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-edit-event',
  standalone: true,
  imports: [ SidebarComponent,ReactiveFormsModule,CommonModule],
  templateUrl: './edit-event.component.html',
  styleUrls: ['./edit-event.component.scss']
})
export class EditEventComponent implements OnInit {
  eventForm: FormGroup;
  eventId: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventService: EventService,
    private fb: FormBuilder
  ) {
    this.eventForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      date: ['', Validators.required, this.dateValidator],
      location: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.eventId = +this.route.snapshot.paramMap.get('eventId')!;
    this.loadEventDetails();
  }


  dateValidator(control: any) {
    if (!control.value) {
      return null; 
    }
  
    const today = new Date();
    today.setHours(0, 0, 0, 0);
  
    const selectedDate = new Date(control.value);
    selectedDate.setHours(0, 0, 0, 0);
  
    return selectedDate >= today ? null : { invalidDate: true };
  }
  loadEventDetails(): void {
    this.eventService.getEventById(this.eventId).subscribe((event: Event) => {
      this.eventForm.patchValue({
        title: event.title,
        description: event.description,
        date: event.date,
        location: event.location
      });
    });
  }

  updateEvent(): void {
    if (this.eventForm.valid) {
      this.eventService.editEvent(this.eventId, this.eventForm.value).subscribe(() => {
        alert('Event updated successfully!');
        this.router.navigate(['/my-event']); 
      });
    }
  }

  cancelEdit(): void {
    this.router.navigate(['/my-event']); 
  }
}
