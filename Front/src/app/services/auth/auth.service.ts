import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient,private router: Router) { }
  saveToken(token: string): void {
    localStorage.setItem('token', token);

    setTimeout(() => {
      this.logout();
    }, 3600000); // 1 heure 
  }


  isLoggedIn(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      const token = localStorage.getItem('token');
      if (!token) {
        resolve(false); 
      }

      const httpOptions = {
        headers: new HttpHeaders({
          'Accept': 'application/json',
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}` 
        })
      };
      console.log(`Bearer ${token}`);
      // Verification de jeton
      this.http.get<any>('http://localhost:8080/auth/user/verifyToken', httpOptions).subscribe(
        response => {
          console.log('User authenticated successfully:', response);
          resolve(true); 
        },
        error => {
          console.error('User authentication failed:', error);
          resolve(false); 
        }
      );
    });
  }
  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/']); 
  }



}
