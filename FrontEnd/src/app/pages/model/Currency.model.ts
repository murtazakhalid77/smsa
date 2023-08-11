export interface ICurrencyDto {
    id?: number;
    currencyFrom?: string,
    currencyTo?: string,
    isPresent?: boolean,
  }

  
  export class Currency implements ICurrencyDto {
    constructor(
      public  id?: number,
      public currencyFrom?: string,
      public  currencyTo?: string,
      public isPresent?: boolean,
    ) {}
    
  }