import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { CustomerService, EntityCustomerResponseType, EntityAllCustomersResponseType} from 'src/app/services/orders.service';
import { Customer, ICustomer } from './customer.model';
import { PagingConfig } from '../model/paging-config-model';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'src/Environments/environment';

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
  currentPage:number  = 0;
  itemsPerPage: number = 10;
  totalItems?: string;
  _url = environment.backend;


  constructor(private customerService: CustomerService, private router: Router, private loginService: LoginService, private route: ActivatedRoute, private http: HttpClient) { 

    // this.customerService.getCustomers(this.currentPage - 1, this.itemsPerPage).subscribe(
    //   (res: EntityAllCustomersResponseType) => {
    //     if(res && res.body){
    //       this.customers = res.body;
    //       this.originalCustomers = this.customers!;
    //       debugger;
    //       this.totalItems = res.headers.has('X-Total-Count')
    //     ? res.headers.get('X-Total-Count')!
    //     : '1'; // Set a default value if X-Total-Count is not present

    //       this.pagingConfig = {
    //         itemsPerPage: this.itemsPerPage,
    //         currentPage: this.currentPage,
    //         totalItems: Number(this.totalItems)
    //       }
    //     } 
    //   },
    //   (error) => {
    //     // Handle the error if needed
    //     console.error('Error fetching customers:', error);
    //   }
    // );


    this.getCustomers(this.currentPage, this.itemsPerPage);
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params.hasOwnProperty('accountNumbers')) {
        this.accountNumbers = params['accountNumbers'];
      }
    });
      // this.getCustomers(1, 5);
  }

  addCustomer() {
    this.router.navigateByUrl('/customer')
  }

  getCustomers(page: any, size: any) {
    this.customerService.getCustomers(page, size).subscribe(
      (res: EntityAllCustomersResponseType) => {
        if(res && res.body){
          this.customers = res.body;
          this.originalCustomers = this.customers!;
          this.totalItems = res.headers.get('X-Total-Count') ?? '';
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
          this.getCustomers(this.currentPage, this.itemsPerPage);
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

  changePage(value: any){
    this.getCustomers(value.pageIndex, this.itemsPerPage);
  }

  downloadFile() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/excel/customer`, {
        responseType: 'blob', // Important: responseType should be 'blob'
        headers,
      })
      .subscribe((response: any) => {
        const blob = new Blob([response], { type: 'application/octet-stream' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'sales_report.xlsx'; // Set the desired filename here
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      });
  }

}