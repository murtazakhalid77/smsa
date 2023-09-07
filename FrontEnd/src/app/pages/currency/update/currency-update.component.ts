import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Currency, ICurrencyDto } from '../../model/Currency.model';
import { CurrencyService, EntityCurrencyResponseType } from '../service/currency.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-currency-update',
  templateUrl: './currency-update.component.html',
  styleUrls: ['./currency-update.component.css']
})
export class CurrencyUpdateComponent {
  currencyForm!: FormGroup
  currency?: ICurrencyDto;
  id: any;

  constructor(
    private formbuilder: FormBuilder, 
    private currencyService: CurrencyService, 
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
    ) { }

    ngOnInit(): void {

      this.currencyForm = this.formbuilder.group({
        currencyFrom: ['', [Validators.required]],
        currencyTo: ['', [Validators.required]],
        conversionRate: ['', Validators.required],
        isActive: ['', [Validators.required]],
      })

      this.route.queryParams.subscribe( params => {
        this.id = params['id'];
        if(this.id!=null){
          this.updateForm(this.id);
        }
      });

    }

  
    updateForm(id: any){

      this.getCurrencyById(id).subscribe(res =>{
        if(res){
          this.currency = res;
          this.currencyForm.patchValue(this.currency);
          this.currencyForm.get('isActive')?.setValue(this.currency?.isPresent);
          this.currencyForm.get('currencyTo')?.disable()
          this.currencyForm.get('currencyFrom')?.disable()
        }
      })
  
    }

    getCurrencyById(id?: any): Observable<Currency | null> {
      return this.currencyService.getCurrencyById(id).pipe(
        map((res: EntityCurrencyResponseType) => {
          if (res && res.body) {
            return res.body;
          }
          return null;
        })
      );
    }
  
    
  submit(currencyForm: FormGroup){
    let currency = {
      id: this.id,
      currencyFrom: currencyForm.value.currencyFrom !== undefined ? currencyForm.value.currencyFrom : this.currency?.currencyFrom,
      currencyTo: currencyForm.value.currencyTo !== undefined ? currencyForm.value.currencyTo : this.currency?.currencyTo,
      conversionRate: currencyForm.value.conversionRate,
      isPresent: currencyForm.value.isActive
    };
    

    if(this.id!=null ){
      this.updateCurrency(currency);
    }else{
      this.createCurrency(currency);
    }
  }

  updateCurrency(currency: Currency) {
      this.currencyService.updateCurrency(currency).subscribe((res: any)=>{
        if(res){
          this.router.navigateByUrl('/currency/view')
        }
      })
  }

  createCurrency(currency: Currency){
    this.currencyService.addCurrency(currency).subscribe((res: any)=>{
      if(res){
        this.router.navigateByUrl('/currency/view')
      }
    },
    error =>{
      this.toastr.error(error.error.body);
    })
  }
}
