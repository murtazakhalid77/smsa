export interface Itransaction{
      id:number
     accountNumber :String
      currentStatus:String;
      mailSent:boolean;
      downloadUrl:String;
      sheetId:String;
  }
  export class Transaction implements Itransaction {
    constructor(
       public id:number,
       public accountNumber :String,
       public currentStatus:String,
       public mailSent:boolean,
       public downloadUrl:String,
       public sheetId:String
    ) {}
     
  }