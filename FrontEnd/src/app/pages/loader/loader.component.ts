import { Component } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent {
  
  loader: boolean = false;

  constructor(private loginService: LoginService){
    this.loginService.loader.subscribe(res => {
      this.loader = res; 
    })
  }

  ngOnInit(){}

}
