import { Component } from '@angular/core';
import { SaleReportService } from 'src/app/services/report-service.service';
import { ISalesReport } from '../model/sales-report.model.';
import { ExcelService } from 'src/app/services/excel-service.service';
import { saveAs } from 'file-saver';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/Environments/environment';
import { FileService } from 'src/app/services/file-service.service';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent {

  startDate?: any;
  endDate?: any;
  dateInputs: boolean = false;
  invoiceTo?: any;
  invoiceFrom?: any;
  invoiceInputs: boolean = true;
  awb: boolean = false;
  awbs: any;
  salesReport?: ISalesReport[];
  selectedSearchOption: string = 'invoice';
  criteria: any;
  currentPage:number = 0;
  itemsPerPage: number = 10;
  totalItems?: string;
  searchText?: string;

  _url = environment.backend;

  constructor(private salesReportService: SaleReportService, 
    private excelService: ExcelService, 
    private http: HttpClient,
    private router:Router,
    private toastr: ToastrService,
    private fileService: FileService,){}

    ngOnInit(){
      this.onSearchOptionChange('invoice');
    }

  salesReportDownload(){}

  onSearchOptionChange(selectedOption: string) {

    this.salesReport = [];
    if (selectedOption === 'invoice') {
      this.startDate = '';
      this.searchText = '';
      this.endDate = '';
      this.dateInputs = false;
      this.awb = false;
      this.invoiceInputs = true;
    } else if (selectedOption === 'date') {
      this.invoiceTo = '';
      this.invoiceFrom = ''
      this.searchText = '';
      this.invoiceInputs = false;
      this.awb = false;
      this.dateInputs = true;
    }else if(selectedOption === 'awbs'){
      this.searchText = '';
      this.invoiceInputs = false;
      this.dateInputs = false;
      this.awb = true;
    }
  }

  disableDateInputs() {
    if(this.invoiceTo!='' || this.invoiceFrom!=''){
      this.dateInputs = true;
    }else{
      this.dateInputs = false
    }
  }

      getSalesReport(page?: any, size?: any){
        debugger;
        this.salesReport = [];

        const searchSalesReport = {
          invoiceTo: this.selectedSearchOption === 'invoice' ? this.invoiceTo : null,
          invoiceFrom: this.selectedSearchOption === 'invoice' ? this.invoiceFrom : null,
          startDate: this.selectedSearchOption === 'date' ? this.startDate : null,
          endDate: this.selectedSearchOption === 'date' ? this.endDate : null,
          awbs: this.selectedSearchOption === 'awbs' ? this.awbs : null,
          mapper: 'SALES_REPORT',
          page: page,
          size: size,
          search: this.searchText
        };

        // Check if any search criteria is provided
        if ((this.selectedSearchOption === 'invoice' && (this.invoiceTo || this.invoiceFrom)) ||
            (this.selectedSearchOption === 'date' && (this.startDate || this.endDate)) ||
            (this.selectedSearchOption === 'awbs' && (this.awbs))) {

          this.salesReportService.getSalesReport(searchSalesReport).subscribe(res => {
            if (res.body?.length !== 0 && res) {
              this.totalItems = res.headers.get('X-Total-Count') ?? '';
              this.salesReport = res.body!;
            } else {
              this.toastr.error("No Data Found");
            }
          },
          error =>{
            this.toastr.error(error.error.body);
          }
          );
        } else {
          this.toastr.error("Please provide valid search criteria");
        }
  }


  changePage(value: any){
    this.getSalesReport(value.pageIndex, this.itemsPerPage);
  }


  downloadFile() {
    const salesReportIds = this.salesReport?.map(report => report.id); // Replace with your desired salesReportIds
    const params = new HttpParams().set('salesReportIds', salesReportIds!.join(','));
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/excel/export/excel`, {
        responseType: 'blob', // Important: responseType should be 'blob'
        headers,
        params: params
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

  downloadExcel(saleReport: any){
    this.fileService.downloadExcel(saleReport, '_sales_report.xlsx');
  }

  downloadPdf(saleReport: any){
    this.fileService.downloadPdf(saleReport);
  }
  

}
