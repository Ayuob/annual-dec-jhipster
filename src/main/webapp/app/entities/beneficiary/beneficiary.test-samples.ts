import { EntitlementType } from 'app/entities/enumerations/entitlement-type.model';

import { IBeneficiary, NewBeneficiary } from './beneficiary.model';

export const sampleWithRequiredData: IBeneficiary = {
  id: 54185,
  entitlementType: EntitlementType['MEDICAL'],
};

export const sampleWithPartialData: IBeneficiary = {
  id: 65260,
  entitlementType: EntitlementType['OTHER'],
};

export const sampleWithFullData: IBeneficiary = {
  id: 61006,
  entitlementType: EntitlementType['MEDICAL'],
  entitlementDetails: 'Metal application Mouse',
};

export const sampleWithNewData: NewBeneficiary = {
  entitlementType: EntitlementType['MEDICAL'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
