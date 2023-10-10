import { NumberValueAccessor } from "@angular/forms";

export interface ISalesReport {
    id?: number;
    invoiceNumber?: string;
    customerAccountNumber?: string;
    customerName?: string;
    customerRegion?: string;
    period?: string;
    totalChargesAsPerCustomerDeclarationForm?: number;
    smsaFeeCharges?: number;
    vatOnSmsaFees?: number;
    totalAmount?: number;
    invoiceCurrency?: number;
    excelDownload?:String;
    pdfDownload?:String;
    createdAt?: Date;   
  }

  export class SalesReport implements ISalesReport {
    constructor(
        public invoiceNumber?: string,
        public customerAccountNumber?: string,
        public customerName?: string,
        public customerRegion?: string,
        public period?: string,
        public totalChargesAsPerCustomerDeclarationForm?: number,
        public smsaFeeCharges?: number,
        public vatOnSmsaFees?: number,
        public totalAmount?: number,
        public invoiceCurrency?: number,
        public excelDownload?:String,
        public pdfDownload?:String,
        public createdAt?: Date,
    ) {}
  }