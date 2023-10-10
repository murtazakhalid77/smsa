import { Custom } from "./excel-import.model";

export interface IsheetHistory{
      id:number
      name :String,
      excelDownload: string,
      uniqueUUid:String;
      isEmailSent:String;
      startDate:String;
      endDate:String;
      custom :Custom;
      invoiceDate:String;
  }
  export class sheetHistory implements IsheetHistory {
    constructor(
       public id: number,
       public name: string,
       public excelDownload: string,
       public uniqueUUid: String,
       public isEmailSent: String,
       public startDate: String,
       public endDate: String,
       public custom: Custom,
       public invoiceDate: String
    ) {}
     
  }