export interface    ICustom {
    id?: number;
    customPort?: string,
    custom?: string,
    smsaFeeVat?: number,
    currency?: string,
    present?: boolean
  }

  export class Custom implements ICustom {
    constructor(
        public id?: number,
        public customPort?: string,
        public custom?: string,
        public smsaFeeVat?: number,
        public currency?: string,
        public present?: boolean
    ) {}
  }