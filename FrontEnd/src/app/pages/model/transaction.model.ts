export interface Itransaction{
      id:number
      accountNumber: String;
      invoiceNumber: string
      currentStatus: String;
      excelDownload: String;
      pdfDownload: String;
      mailSent: boolean;
      sheetId :String;
  }
  export class Transaction implements Itransaction {
    constructor(
       public id:number,
       public accountNumber: String,
       public invoiceNumber: string,
       public currentStatus: String,
       public excelDownload: String,
       public pdfDownload: String,
       public mailSent: boolean,
       public sheetId: String
    ) {}
     
  }