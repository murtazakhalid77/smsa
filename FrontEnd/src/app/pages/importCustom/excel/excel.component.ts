import { Component, Renderer2 } from '@angular/core';
import { ImportService } from 'src/app/services/import.service';
import { UploadExcelService } from 'src/app/services/upload-excel.service';
import * as XLSX from 'xlsx';
import { read, utils, WorkBook, WorkSheet } from 'xlsx';
import { OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { NavigationExtras, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { FileService } from 'src/app/services/file-service.service';
import { saveAs } from 'file-saver';
import { ICustom } from '../../custom-port/custom.model';
import { IExcelImportDto } from '../../model/excel-import.model';
import { CustomService, EntityAllCustomsResponseType } from '../../custom-port/service/custom-port.service';
import { ImportCustomService } from './import-custom.service';
import { NumberData } from '../../import/importCustom/excel';
@Component({
  selector: 'app-excel',
  templateUrl: './excel.component.html',
  styleUrls: ['./excel.component.css'],
})
export class ExcelComponent {
  dataArray: any = [];
  arr: any = []
  flag: Boolean = false;
  fileToUpload: any;
  dialogue: boolean = true
  tableHeader: any = []
  customs?: ICustom[];
  selectedCustomPort?: string = '';
  startDate?: string;
  endDate?: string;
  invoiceDate?: string;
  customVat?: any;
  isModalOpen = false;
  errorModalOpen = false;
  openModelTable: Boolean = false
  accountNumbers: any;
  excelImportDto: IExcelImportDto = {};
  duplicateExceptionMessage?: string = 'There was a duplication of these AWBs'
  duplicateAwbsMessage?: string = ''
  data: any = null;
  @ViewChild('modalId') modalElement?: ElementRef; // making it optional
  userInput: { [key: string]: string | null } = {};

  openTable() {
    this.openModal();
  }
  numberData: any = [];

  headers: any = ['mawb', 'manifest date', 'account number', 'awb', 'ordernumber', 'origin', 'destination', 'shipper name', 'consignee name', 'weight', 'declared value', 'value (custom)', 'custom declaration', 'other', 'ref#', 'custom declaration date'];

  constructor(private service: UploadExcelService, private importCustomService: ImportCustomService, private renderer: Renderer2, private toastr: ToastrService, private customService: CustomService, private router: Router, private datePipe: DatePipe, private fileService: FileService) { }
  reloadPage() {
    window.location.reload()
  }

  ngOnInit(): void {
    this.getCustomsPresent();

  }

  onSubmit(): void {
    this.excelImportDto.customPlusExcel=true;
    this.importExcel(false)

   

    ;
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

cancelDialogueBox(){
  this.dialogue = false
}
  onFileChange(event: any) {
    this.fileToUpload = event.target.files[0];
    // this.openModelTable = true;
    this.readFile(this.fileToUpload);
    // if (this.fileToUpload) {
    //   this.openModal1();
    // }
  }
  openModal1(): void {
    if (this.modalElement && this.modalElement.nativeElement) {
      this.renderer.setStyle(this.modalElement.nativeElement, 'display', 'block');
      this.renderer.addClass(document.body, 'modal-open');
      this.renderer.addClass(this.modalElement.nativeElement, 'show');
      // Other necessary modal setup
    }
  }

  closeModal1(): void {
    this.excelImportDto.customPlusExcel = true;
    if (this.modalElement && this.modalElement.nativeElement) {
      this.renderer.setStyle(this.modalElement.nativeElement, 'display', 'none');
      this.renderer.removeClass(document.body, 'modal-open');
      this.renderer.removeClass(this.modalElement.nativeElement, 'show');
      // Other necessary modal teardown
    }
  }
  private addModalBackdrop() {
    const backdrop = this.renderer.createElement('div');
    this.renderer.addClass(backdrop, 'modal-backdrop');
    this.renderer.addClass(backdrop, 'fade');
    this.renderer.addClass(backdrop, 'show');
    this.renderer.appendChild(document.body, backdrop);
  }

  private removeModalBackdrop() {
    const backdrops = document.getElementsByClassName('modal-backdrop');
    if (backdrops.length) {
      document.body.removeChild(backdrops[0]);
    }
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
  openModal(): void {
    if (this.modalElement && this.modalElement.nativeElement) {
      this.renderer.setStyle(this.modalElement.nativeElement, 'display', 'block');
      this.renderer.addClass(document.body, 'modal-open');
      // You might not need 'show' here if you're manually setting 'display' to 'block', but it depends on your styling setup
      this.renderer.addClass(this.modalElement.nativeElement, 'show');
      // Other necessary modal setup
    }
  }
  
  closeModal(): void {
    if (this.modalElement && this.modalElement.nativeElement) {
      this.renderer.setStyle(this.modalElement.nativeElement, 'display', 'none');
      this.renderer.removeClass(document.body, 'modal-open');
      this.renderer.removeClass(this.modalElement.nativeElement, 'show');
      // Other necessary modal teardown
    }
  }

  // getNumberData(): void {
  //   this.importCustomService.getNumberData().subscribe({
  //     next: (data) => this.numberData = data,
  //     error: (err) => console.error('Failed to fetch number data:', err),
  //   });
  // }

  async importExcel(flag1: boolean = true) {
    // this.toastr.clear();
    this.excelImportDto.customPlusExcel=true
    let flag = true;
    for (let i = 0; i < this.dataArray.length; i++) {
      if (!this.headers.includes(this.dataArray[i].toLowerCase())) {
        flag = false;
        break;
      }
    }
    const custom = this.getCustom(this.selectedCustomPort);

    if (custom) {
      if (this.startDate || this.endDate) {
        if (this.fileToUpload !== undefined) {
          if (flag) {
            this.excelImportDto = {
              id: custom.id,
              customPort: custom.customPort,
              custom: custom.custom,
              smsaFeeVat: custom.smsaFeeVat,
              present: custom.present,
              date1: this.startDate != null ? new Date(this.startDate!) : undefined,
              date2: this.endDate != null ? new Date(this.endDate!) : undefined,
              date3: this.invoiceDate != null ? new Date(this.invoiceDate!) : undefined,
              customPlusExcel: this.excelImportDto.customPlusExcel,
            };


            try {
              debugger
              const res = await this.service.uploadFile(this.fileToUpload, this.excelImportDto, this.userInput);

              if (flag1) {
                if (res) {
                  this.numberData = res;
                  this.openModelTable = true;
                  this.openModal1();
                  this.numberData.forEach((item: any) => {
                    this.userInput[item] = null; // You can assign any value to the key if needed
                  });
                  this.toastr.success("kindly fill out all the table values");
                }
              }else{
                this.accountNumbers = res.accountNumbers;
                this.toastr.success(res.accountNumbers);
                if (this.accountNumbers.length > 0) {
                  this.isModalOpen = true;
                }else{
                  this.toastr.success("Sheet Uploaded succesfully");
                }
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

          } else {
            this.toastr.error('The format of the file is Incorrect')
          }
        } else {
          this.toastr.error('Please select file')
        }
      } else {
        this.toastr.error('Please select atleast one date')
      }
    } else {
      this.toastr.error('Please select custom port')
    }

  }

  downloadFile(filename: string): void {
    this.fileService
      .download()
      .subscribe(blob => saveAs(blob, filename));
  }



  navigateToCustomer() {
    const navigationExtras: NavigationExtras = {
      queryParams: { accountNumbers: this.accountNumbers },
    };
    this.router.navigate(['/customer/view'], navigationExtras);
  }

  closeErrorModalModal() {
    this.errorModalOpen = false;
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
