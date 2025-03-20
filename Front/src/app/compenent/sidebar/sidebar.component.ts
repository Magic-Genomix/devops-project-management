import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {  RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule,  RouterLink, RouterLinkActive],

  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

  constructor(private authService: AuthService) { }



  logout(): void {
    this.authService.logout(); }

}
