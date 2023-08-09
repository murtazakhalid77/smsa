import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CustomerService, EntityCustomerResponseType, EntityAllCustomersResponseType} from 'src/app/services/orders.service';
import { Customer, ICustomer } from './customer.model';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  customers?: ICustomer[];
  customer?: ICustomer;
  sortedOrders: any;
  editOrderArray: any
  tableData: Boolean = false
  currentUser: any
  originalCustomers: Customer[] = [];
  accountNumbers: any = null;

  constructor(private customerService: CustomerService, private router: Router, private loginService: LoginService, private route: ActivatedRoute,) { }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params.hasOwnProperty('accountNumbers')) {
        this.accountNumbers = params['accountNumbers'];
      }
    });
      this.getCustomers();
  }

  addCustomer() {
    this.router.navigateByUrl('/customer')
  }

  getCustomers() {
    this.customerService.getCustomers().subscribe(
      (res: EntityAllCustomersResponseType) => {
        if(res && res.body){
          this.customers = res.body;
          this.originalCustomers = this.customers!;
        } 
      },
      (error) => {
        // Handle the error if needed
        console.error('Error fetching customers:', error);
      }
    );
  }

  // statusSorting(find: any) {
  //   this.customerService.statusSorting(find).subscribe(res => {
  //     this.customers = res
  //   })
  // }

  updateCustomer(accountNumber?: any){

    this.router.navigate(['/customer'], { queryParams: { accountNumber: accountNumber } });
  }

  deleteCustomer(accountNumber?: any) {
    this.customerService.deleteCustomer(accountNumber).subscribe(
      (res: EntityCustomerResponseType) => {

        if(res && res.body){
          this.customer = res.body;
          this.getCustomers();
        } 
      },
      (error) => {
        // Handle the error if needed
        console.error('Error fetching customers:', error);
      }
    );
  }

  searchCustomer(inputElement: EventTarget | null) {
    if (inputElement instanceof HTMLInputElement) {
      const searchText = inputElement.value.trim().toLowerCase();
      if (searchText === '') {
        // If the input field is empty, reset the customers list to its original state
        this.customers = [...this.originalCustomers];
      } else {
        // Filter the customers based on the entered text
        this.customers = this.originalCustomers.filter((customer) =>
          customer.accountNumber?.toLowerCase().includes(searchText)
        );
      }
    }
  }
  
  

}