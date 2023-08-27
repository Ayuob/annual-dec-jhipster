import { IFamilyMember } from 'app/entities/family-member/family-member.model';
import { IAnnualDeclaration } from 'app/entities/annual-declaration/annual-declaration.model';
import { EntitlementType } from 'app/entities/enumerations/entitlement-type.model';

export interface IBeneficiary {
  id: BeneficiaryId;
  entitlementType?: EntitlementType | null;
  entitlementDetails?: string | null;
  familyMembers?: Pick<IFamilyMember, 'id' | 'name' | 'nationalNumber'> | null;
  annualDeclaration?: Pick<IAnnualDeclaration, 'id' | 'submissionDate'> | null;
}

// export type NewBeneficiary = Omit<IBeneficiary, 'id'> & { id: null };

export type NewBeneficiary = Omit<IBeneficiary, 'id'> & {
  id: BeneficiaryId;
};

export type BeneficiaryId = {
  familyMemberId: number;
  annualDeclarationId: number;
};

//v2
// export type NewBeneficiary = Omit<IBeneficiary, 'familyMembersId' | 'annualDeclarationId'> & {
//    familyMembersId: null, annualDeclarationId: null
// };
