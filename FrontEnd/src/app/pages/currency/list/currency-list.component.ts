import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CustomService } from '../../custom-port/service/custom-port.service';
import { CurrencyService, EntityAllCurrencyResponseType } from '../service/currency.service';
import { ICurrencyDto } from '../../model/Currency.model';

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.css']
})
export class CurrencyListComponent {

  currency?: ICurrencyDto[];

  constructor(private router: Router, private loginService: LoginService, private currencyService: CurrencyService){}

  ngOnInit(){
    this.getCurrency();
  }

  getCurrency(){
    this.currencyService.getCurrency().subscribe(
      (res: EntityAllCurrencyResponseType) => {
        if(res && res.body){
          this.currency = res.body;
        } 
      },
      (error) => {
        // Handle the error if needed
        console.error('Error fetching customers:', error);
      }
    );
  }

  addCurrency(){
    this.router.navigateByUrl('/currency')
  }

  
  updateCurrency(id: any){
    this.router.navigate(['/currency'], { queryParams: { id: id } });
  }

  showHistory(id: any){
    this.router.navigate(['/currency/history'], { queryParams: { id: id } });
  }


}
