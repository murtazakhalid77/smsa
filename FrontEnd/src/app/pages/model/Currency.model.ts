export interface ICurrencyDto {
    id?: number;
    currencyFrom?: string,
    currencyTo?: string,
    conversionRate?: string,
    isPresent?: boolean,
  }

  
  export class Currency implements ICurrencyDto {
    constructor(
      public  id?: number,
      public currencyFrom?: string,
      public  currencyTo?: string,
      public conversionRate?: string,
      public isPresent?: boolean,
    ) {}
    
  }