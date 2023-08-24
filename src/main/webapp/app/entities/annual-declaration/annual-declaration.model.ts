import dayjs from 'dayjs/esm';
import { ISocialSecurityPensioner } from 'app/entities/social-security-pensioner/social-security-pensioner.model';
import { IFamilyMember } from 'app/entities/family-member/family-member.model';
import { DeclarationStatus } from 'app/entities/enumerations/declaration-status.model';

export interface IAnnualDeclaration {
  id: number;
  submissionDate?: dayjs.Dayjs | null;
  status?: DeclarationStatus | null;
  pensioner?: Pick<ISocialSecurityPensioner, 'id' | 'nationalNumber'> | null;
  familyMembers?: Pick<IFamilyMember, 'id' | 'nationalNumber'>[] | null;
}

export type NewAnnualDeclaration = Omit<IAnnualDeclaration, 'id'> & { id: null };
