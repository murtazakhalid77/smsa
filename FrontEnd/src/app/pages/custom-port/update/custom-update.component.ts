import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomService, EntityCustomResponseType } from '../service/custom-port.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Custom, ICustom } from '../custom.model';
import { Observable, map } from 'rxjs';
import { CurrencyService } from '../../currency/service/currency.service';


@Component({
  selector: 'app-custom-update',
  templateUrl: './custom-update.component.html',
  styleUrls: ['./custom-update.component.css']
})
export class CustomUpdateComponent implements OnInit{

  customForm!: FormGroup
  custom?: ICustom;
  id: any;
  vats = [0, 5, 15];
  currencies?: any = ['USD', 'SAR', 'BHD', 'JOD', 'KWD', 'QAR', 'OMR', 'AED'];

  constructor(
    private formbuilder: FormBuilder, 
    private customService: CustomService, 
    private router: Router,
    private route: ActivatedRoute,
    private currencyService: CurrencyService
    ) { }

  ngOnInit(): void {

    // this.getDistinctCurrencies();

    this.customForm = this.formbuilder.group({
      customPort: ['', [Validators.required]],
      custom: ['', [Validators.required]],
      smsaFeeVat: ['', [Validators.required]],
      isActive: ['', [Validators.required]],
      currency: ['', [Validators.required]]
    })

    this.route.queryParams.subscribe( params => {
      this.id = params['id'];
      if(this.id!=null){
        this.updateForm(this.id);
      }
    });
  }

  updateForm(id: any){

    this.getCustomById(id).subscribe(res =>{
      if(res){
        this.custom = res;
        this.customForm.patchValue(this.custom);
        this.customForm.get('isActive')?.setValue(this.custom.present);
      }
    })

  }

  getCustomById(id?: any): Observable<Custom | null> {
    return this.customService.getCustomById(id).pipe(
      map((res: EntityCustomResponseType) => {
        if (res && res.body) {
          return res.body;
        }
        return null;
      })
    );
  }

  getDistinctCurrencies(){
    this.currencyService.getDistinctCurrencies().subscribe(res =>{
      if(res){
        this.currencies = res.body;
      }
    })
  }

  submit(customForm: FormGroup){
    let custom = {
      id: this.id,
      customPort: customForm.value.customPort,
      custom: customForm.value.custom,
      smsaFeeVat: customForm.value.smsaFeeVat,
      currency: customForm.value.currency,
      present: customForm.value.isActive
    }

    console.log(custom)

    if(this.id!=null ){
      this.updateCustom(custom);
    }else{
      this.createCustom(custom);
    }
  }
  updateCustom(custom: Custom) {
    this.customService.updateCustom(custom).subscribe((res: any)=>{
      if(res){
        this.router.navigateByUrl('/custom/view')
      }
    })
  }

  createCustom(custom: Custom){
    this.customService.addCustom(custom).subscribe((res: any)=>{
      if(res){
        this.router.navigateByUrl('/custom/view')
      }
    })
  }

}
