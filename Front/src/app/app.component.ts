import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';



@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive,HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  
  title = 'app-event';
  constructor(private router: Router) {}

  isNav(): boolean {
    const URL: string = this.router.url;
    const navRoutes: string[] = ['/', '/login', '/registration'];
    return navRoutes.includes(URL);
  }
}
