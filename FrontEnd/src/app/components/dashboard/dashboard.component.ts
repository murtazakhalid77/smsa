import { Component, OnInit } from '@angular/core';
import { DashboardServiceService } from 'src/app/services/dashboard-service.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  cardsArray: any;

  constructor(private service: DashboardServiceService ,
              private loginService: LoginService) { }

  ngOnInit(): void {
   
  }
 

}