import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CurrencyService } from '../service/currency.service';
import { ICurrencyHistory } from '../../model/currency-history.model';

@Component({
  selector: 'app-currency-history',
  templateUrl: './currency-history.component.html',
  styleUrls: ['./currency-history.component.css']
})
export class CurrencyHistoryComponent {

  id?: any;
  currencyHistory?: ICurrencyHistory[];
  
  constructor(private router: Router, private loginService: LoginService, private route: ActivatedRoute, private currencyService: CurrencyService){}

  ngOnInit(){
    this.route.queryParams.subscribe( params => {
      this.id = params['id'];
      if(this.id!=null){
       this.currencyService.getCurrencyHistory(this.id).subscribe(res =>{
        if(res && res.body){
          this.currencyHistory = res.body;
        }
       });
      }
    });
  }
}
