import dayjs from 'dayjs/esm';

export interface ISocialSecurityPensioner {
  id: number;
  nationalNumber?: string | null;
  pensionNumber?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  address?: string | null;
}

export type NewSocialSecurityPensioner = Omit<ISocialSecurityPensioner, 'id'> & { id: null };
