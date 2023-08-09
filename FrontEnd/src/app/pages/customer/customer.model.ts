export interface ICustomer {
    id?: number;
    accountNumber?: string;
    invoiceCurrency?: string;
    currencyRateFromSAR?: string;
    smsaServiceFromSAR?: string;
    email?: string;
    nameArabic?: string;
    nameEnglish?: string;
    vatNumber?: string;
    address?: Date;
    poBox?: boolean;
    country?: string;
    present?: boolean
    status?: boolean
  }

  export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public accountNumber?: string,
        public invoiceCurrency?: string,
        public currencyRateFromSAR?: string,
        public smsaServiceFromSAR?: string,
        public email?: string,
        public customerNameArabic?: string,
        public nameEnglish?: string,
        public vatNumber?: string,
        public address?: Date,
        public poBox?: boolean,
        public country?: string,
        public present?: boolean,
        public status?: boolean
    ) {}
  }