import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'Pixorr Dashboard';
  loggedIn= sessionStorage.getItem('currentUser');
  isAdmin=false;
}
