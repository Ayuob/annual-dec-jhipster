import dayjs from 'dayjs/esm';

export class Account {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public firstName: string | null,
    public langKey: string,
    public lastName: string | null,
    public login: string,
    public nationalNumber: string,
    public pensionNumber: string,
    public dateOfBirth: dayjs.Dayjs | null,
    public address: string | null,
    public imageUrl: string | null
  ) {}
}
