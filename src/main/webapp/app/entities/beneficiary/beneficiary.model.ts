import { IFamilyMember } from 'app/entities/family-member/family-member.model';
import { IAnnualDeclaration } from 'app/entities/annual-declaration/annual-declaration.model';
import { EntitlementType } from 'app/entities/enumerations/entitlement-type.model';

export interface IBeneficiary {
  id: number;
  entitlementType?: EntitlementType | null;
  entitlementDetails?: string | null;
  familyMembers?: Pick<IFamilyMember, 'id' | 'name'> | null;
  annualDeclaration?: Pick<IAnnualDeclaration, 'id' | 'submissionDate'> | null;
}

export type NewBeneficiary = Omit<IBeneficiary, 'id'> & { id: null };
