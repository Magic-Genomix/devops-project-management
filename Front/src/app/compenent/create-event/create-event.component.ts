import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService } from '../../services/EventService';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-event',
  standalone: true,
  imports: [ReactiveFormsModule, SidebarComponent, CommonModule], 
  templateUrl: './create-event.component.html',
  styleUrls: ['./create-event.component.scss']
})
export class CreateEventComponent {
  eventForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private eventService: EventService,
    private router: Router
  ) {
    this.eventForm = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      date: ['', [Validators.required, this.dateValidator]],
      location: ['', Validators.required],
    });
    
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
  

  createEvent() {
    if (this.eventForm.invalid) {
      this.errorMessage = 'Please fill in all required fields.';
      return;
    }

    const event = this.eventForm.value;

    this.eventService.createEvent(event).subscribe({
      next: () => {
        this.successMessage = 'Event created successfully!';
        alert('Event created successfully!');
        this.router.navigate(['/events']);
      },
      error: () => {
        this.errorMessage = 'Failed to create event. Please try again.';
      }
    });
  }
}
