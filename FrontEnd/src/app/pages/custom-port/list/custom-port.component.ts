import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CustomService, EntityAllCustomsResponseType } from '../service/custom-port.service';
import { ICustom } from '../custom.model';

@Component({
  selector: 'app-custom-port',
  templateUrl: './custom-port.component.html',
  styleUrls: ['./custom-port.component.css']
})
export class CustomPortComponent implements OnInit {

  customs?: ICustom[];


  constructor(private router: Router, private loginService: LoginService, private customService: CustomService) { }

  ngOnInit(): void {
    this.getCustoms();
  }

  getCustoms() {
    this.customService.getCustoms().subscribe(
      (res: EntityAllCustomsResponseType) => {
        if(res && res.body){
          this.customs = res.body;
        } 
      },
      (error) => {
        // Handle the error if needed
        console.error('Error fetching customers:', error);
      }
    );
  }

  updateCustom(id: any){
    this.router.navigate(['/custom'], { queryParams: { id: id } });

  }

  addCustom(){
    this.router.navigateByUrl('/custom')
  }



}
