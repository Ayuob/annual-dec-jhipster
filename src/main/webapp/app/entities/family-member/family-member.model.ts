import dayjs from 'dayjs/esm';
import { ISocialSecurityPensioner } from 'app/entities/social-security-pensioner/social-security-pensioner.model';
import { IAnnualDeclaration } from 'app/entities/annual-declaration/annual-declaration.model';

export interface IFamilyMember {
  id: number;
  nationalNumber?: string | null;
  name?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  gender?: string | null;
  pensioner?: Pick<ISocialSecurityPensioner, 'id' | 'nationalNumber'> | null;
  annualDeclarations?: Pick<IAnnualDeclaration, 'id' | 'submissionDate'>[] | null;
}

export type NewFamilyMember = Omit<IFamilyMember, 'id'> & { id: null };
