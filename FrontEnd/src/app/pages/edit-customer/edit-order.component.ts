import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from 'src/app/services/orders.service';

@Component({
  selector: 'app-edit-order',
  templateUrl: './edit-order.component.html',
  styleUrls: ['./edit-order.component.css']
})
export class EditOrderComponent implements OnInit {
ngOnInit(): void {
  
}
  // editOrderForm!: FormGroup
  // orderArray: any = []
  // id: any
  // order: any
  // flag: boolean = false

  // constructor(private formbuilder: FormBuilder, private route: ActivatedRoute, private service: CustomerService, private router: Router) { }

  // ngOnInit(): void {
  //   this.editOrderForm = this.formbuilder.group({
  //     account: ['', [Validators.required]],
  //     invoice: ['', [Validators.required]],
  //     currencyRate: ['', [Validators.required]],
  //     isActive: ['', [Validators.required]],
  //     smsaServiceFee: ['', [Validators.required]],
  //     nameArabic: ['', [Validators.required]],
  //     nameEnglish: ['', [Validators.required]],
  //     customerVAT: ['', [Validators.required]],
  //     number: ['', [Validators.required]],
  //     address: ['', [Validators.required]],
  //     poBox: ['', [Validators.required]],
  //     country: ['', [Validators.required]]
  //   })

  //   this.id = Number(this.route.snapshot.paramMap.get("id"))
  //   this.getById(this.id);
  // }

  // getById(id: any) {
  //   this.service.getOrderById(id).subscribe(res => {
  //     this.orderArray.push(res)
  //     this.editOrderForm.patchValue({
  //       account: this.orderArray[0].account,
  //       invoice: this.orderArray[0].invoice,
  //       currencyRate: this.orderArray[0].currencyRate,
  //       isActive: this.orderArray[0].isActive,
  //       smsaServiceFee: this.orderArray[0].smsaServiceFee,
  //       nameArabic: this.orderArray[0].nameArabic,
  //       nameEnglish: this.orderArray[0].nameEnglish,
  //       customerVAT: this.orderArray[0].customerVAT,
  //       number: this.orderArray[0].number,
  //       address: this.orderArray[0].address,
  //       poBox: this.orderArray[0].poBox,
  //       country: this.orderArray[0].country
  //     })
  //   })
  // }

  // submit(orderForm: FormGroup) {
  //   this.order = {
  //     account: orderForm.value.account,
  //     invoice: orderForm.value.invoice,
  //     currencyRate: orderForm.value.currencyRate,
  //     isActive: orderForm.value.isActive,
  //     smsaServiceFee: orderForm.value.smsaServiceFee,
  //     nameArabic: orderForm.value.nameArabic,
  //     nameEnglish: orderForm.value.nameEnglish,
  //     customerVAT: orderForm.value.customerVAT,
  //     number: orderForm.value.number,
  //     address: orderForm.value.address,
  //     poBox: orderForm.value.poBox,
  //     country: orderForm.value.country
  //   }
  //   for (const key in this.order) {
  //     if (this.order.hasOwnProperty(key) && this.orderArray[0].hasOwnProperty(key) && this.order[key] === this.orderArray[0][key]) {
  //       this.flag = false
  //     } else {
  //       this.flag = true
  //     }
  //   }
  //   if (this.flag) {
  //     this.service.updateOrder(this.id, this.order).subscribe()
  //     this.editOrderForm.reset()
  //     this.service.getCustomers().subscribe(res => {
  //       let newOrderArray = res
  //       this.service.getNewOrders(newOrderArray)
  //     })
  //     this.router.navigateByUrl('/orders')
  //   } else {
  //     alert('Please update the value/values')
  //   }
  // }

}