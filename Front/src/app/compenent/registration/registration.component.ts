import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  userForm: FormGroup;

  constructor(private router: Router,private formBuilder: FormBuilder, private http: HttpClient) { // Injection de HttpClient
    this.userForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.strongPasswordValidator]]
    });
    
  }

  strongPasswordValidator(control: any) {
    if (!control.value) {
      return null; 
    }
  
    const strongPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    return strongPasswordRegex.test(control.value) ? null : { weakPassword: true };
  }
  

  convertEmailToLowerCase() {
    const email = this.userForm.get('email')?.value;
    if (email) {
      this.userForm.get('email')?.setValue(email.toLowerCase(), { emitEvent: false });
    }
  }

  ngOnInit(): void {
    
  }

  onSubmit(): void {


    if (this.userForm.valid) {
      const httpOptions = {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      };
      this.http.post<any>('http://localhost:8080/auth/addNewUser', this.userForm.value,httpOptions).subscribe(
        response => {
          console.log('User added successfully:', response);
          alert('User added successfully');
          this.router.navigate(['/login']);
          
        },
        error => {
          console.log('data:',this.userForm.value);
          console.error('Error adding user:', error);
        }
      );
    }
  }
}