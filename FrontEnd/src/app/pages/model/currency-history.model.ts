export interface ICurrencyHistory{
    id?: number;
    currencyFrom?: string,
    currencyTo?: string,
    conversionRate?: string,
    createdBy?: string,
    createdAt?: string,
    updatedBy?: String,
    updatedAt?: string,
    isPresent?: boolean,
  }

  
  export class CurrencyHistory implements ICurrencyHistory {
    constructor(
      public  id?: number,
      public currencyFrom?: string,
      public  currencyTo?: string,
      public conversionRate?: string,
      public createdBy?: string,
      public createdAt?: string,
      public updatedBy?: String,
      public updatedAt?: string,
      public isPresent?: boolean,
    ) {}
    
  }