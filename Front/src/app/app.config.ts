import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { Routes } from '@angular/router';
import { HomeComponent } from './compenent/home/home.component';
import { LoginComponent } from './compenent/login/login.component';
import { RegistrationComponent } from './compenent/registration/registration.component';
import { DashboardComponent } from './compenent/dashboard/dashboard.component';
import { AuthGuard } from './services/auth/auth.guard';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { EventListComponent } from './compenent/event-list/event-list.component';
import { CreateEventComponent } from './compenent/create-event/create-event.component';
import { MyEventComponent } from './compenent/my-event/my-event.component';
import { EditEventComponent } from './compenent/edit-event/edit-event.component';
const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'events', component: EventListComponent, canActivate: [AuthGuard] },
  { path: 'create-event', component: CreateEventComponent, canActivate: [AuthGuard] },
  { path: 'my-event', component: MyEventComponent, canActivate: [AuthGuard] },
  { path: 'edit-event/:eventId', component: EditEventComponent, canActivate: [AuthGuard] },

];

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),HttpClientModule, provideHttpClient(withFetch())
  ]
};
