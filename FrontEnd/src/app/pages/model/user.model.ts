export interface IUser{
    id?: number;
    name?: string,
    password?: string,
    status?: boolean,
  }

  export class User implements IUser {
    constructor(
        public id?: number,
        public name?: string,
        public password?: string,
        public status?: boolean,
    ) {}
  }