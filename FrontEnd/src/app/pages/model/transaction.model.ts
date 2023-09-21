export interface Itransaction{
      id:number
      accountNumber :String
      currentStatus:String
      excelDownload:String;
      pdfDownload:String;
      mailSent:boolean;
      sheetId:String;
  }
  export class Transaction implements Itransaction {
    constructor(
       public id:number,
       public accountNumber :String,
       public currentStatus:String,
       public excelDownload:String,
       public pdfDownload:String,
       public mailSent:boolean,
       public sheetId:String
    ) {}
     
  }