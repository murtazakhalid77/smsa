import { ImportService } from 'src/app/services/import.service';
import { UploadExcelService } from 'src/app/services/upload-excel.service';
import * as XLSX from 'xlsx';
import { read, utils, WorkBook, WorkSheet } from 'xlsx';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { CustomService, EntityAllCustomsResponseType } from '../custom-port/service/custom-port.service';
import { ICustom } from '../custom-port/custom.model';
import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { NavigationExtras, Router } from '@angular/router';
import { IExcelImportDto } from '../model/excel-import.model';
import { DatePipe } from '@angular/common';
import { FileService } from 'src/app/services/file-service.service';
import { saveAs } from 'file-saver';


@Component({
  selector: 'app-import',
  templateUrl: './import.component.html',
  styleUrls: ['./import.component.css']
})
export class ImportComponent {


  dataArray: any = [];
  arr: any = []
  flag: Boolean = false;
  fileToUpload: any;

  tableHeader: any = []
  customs?: ICustom[];
  selectedCustomPort?: string = '';
  startDate?: string;
  endDate?: string;
  invoiceDate?: string;
  customVat?: any;
  isModalOpen = false;
  errorModalOpen = false;
  accountNumbers:any;
  excelImportDto?: IExcelImportDto = {};
  duplicateExceptionMessage?:string = 'There was a duplication of these AWBs'
  duplicateAwbsMessage?: string = ''

  headers: any = ['mawb', 'manifest date', 'account number', 'awb', 'ordernumber', 'origin', 'destination', 'shipper name', 'consignee name', 'weight', 'declared value', 'value (custom)', 'vat amount', 'custom form', 'other', 'total charges', 'custom declaration', 'ref#', 'custom declaration date'];

  constructor(private service: UploadExcelService, private toastr: ToastrService, private customService: CustomService,     private router:Router, private datePipe: DatePipe, private fileService: FileService) { }

  reloadPage() {
    window.location.reload()
  }

  ngOnInit(): void {
    this.getCustomsPresent();
  }

  getCustom(selectedCustomPort?: string): any | undefined {
    const selectedCustom = this.customs?.find((custom) => custom.customPort === selectedCustomPort);
    return selectedCustom;
  }

  getCustomsPresent() {
    this.customService.getCustomsPresent().subscribe(
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

  onCustomPortSelected(){
    const custom = this.getCustom(this.selectedCustomPort);
    this.customVat = custom.smsaFeeVat;
  }

  onDateChange() {
  }


  onFileChange(event: any) {
    this.fileToUpload = event.target.files[0];
    this.readFile(this.fileToUpload);
  }

  readFile(file: File) {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: 'array' });
      const worksheet = workbook.Sheets[workbook.SheetNames[0]];
      const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });
      this.dataArray = jsonData[0];
    };
    reader.readAsArrayBuffer(file);
  }

  async importExcel() {
  this.toastr.clear();

    let flag = true;
    for(let i = 0; i< this.dataArray.length; i++){
      if(!this.headers.includes(this.dataArray[i].toLowerCase())){
        flag = false;
        break;
      }
    }
    const custom = this.getCustom(this.selectedCustomPort);

    if(custom){
      if(this.startDate || this.endDate){
        if(this.fileToUpload!==undefined){
          if(flag){
            this.excelImportDto = {
              id: custom.id,
              customPort: custom.customPort,
              custom: custom.custom,
              smsaFeeVat: custom.smsaFeeVat,
              present: custom.present,
              date1: this.startDate != null ? new Date(this.startDate!) : undefined,
              date2: this.endDate != null ? new Date(this.endDate!) : undefined,
              date3: this.invoiceDate != null ? new Date(this.invoiceDate!) : undefined
            };
            try {
              const res = await this.service.uploadFile(this.fileToUpload, this.excelImportDto);
              this.toastr.success(res.message);
              this.accountNumbers = res.accountNumbers;
              
              if (this.accountNumbers.length > 0) {
                this.isModalOpen = true;
              }
            } catch (error: any) {
              this.toastr.error(error.error.body);
            
              // if (error.error.body.includes(this.duplicateExceptionMessage)) {
              //   this.duplicateAwbsMessage = error.error.body;
              //   this.errorModalOpen = true;
              // } else {
              //   this.toastr.error(error.error.body);
              // }
            }
            
          }else{
            this.toastr.error('The format of the file is Incorrect')
          }
        }else{
          this.toastr.error('Please select file')
        }
      }else{
        this.toastr.error('Please select atleast one date')
      }
    }else{
      this.toastr.error('Please select custom port')
    }

  }

  downloadFile(filename: string): void {
    this.fileService
      .download()
      .subscribe(blob => saveAs(blob, filename));
  }

  closeModal() {
    this.isModalOpen = false;
  }
  closeErrorModalModal(){
    this.errorModalOpen = false;
  }

  navigateToCustomer(){
    const navigationExtras: NavigationExtras = {
      queryParams: { accountNumbers: this.accountNumbers },
    };
    this.router.navigate(['/customer/view'], navigationExtras);
  }



  dateRangeCreated($event: any) {
        // this.products = this.tempproducts;
        let startDate = $event[0].toJSON().split('T')[0];
        let endDate = $event[1].toJSON().split('T')[0];

        // console.log(startDate);
        // console.log(endDate);
        // this.products = this.products.filter(
        //   m => new Date(m.CreatedDate) >= new Date(startDate) && new Date(m.CreatedDate) <= new Date(endDate)
        // );
      }

}
