import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.component.html',
  styleUrls: ['./home-admin.component.css'],
})
export class DashboardComponent implements OnInit {

  isMenuOpen = false;

  constructor() {}

  ngOnInit(): void {
    window.history.pushState(null, '', window.location.href);
    window.onpopstate = () => {
      window.history.pushState(null, '', window.location.href);
    };
  }

  // Cambia el estado del toggle
  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }
}
