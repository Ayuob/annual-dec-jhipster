import { EntitlementType } from 'app/entities/enumerations/entitlement-type.model';

import { IBeneficiary, NewBeneficiary } from './beneficiary.model';

export const sampleWithRequiredData: IBeneficiary = {
  id: 79627,
  entitlementType: EntitlementType['EDUCATION'],
};

export const sampleWithPartialData: IBeneficiary = {
  id: 54574,
  entitlementType: EntitlementType['PENSION'],
};

export const sampleWithFullData: IBeneficiary = {
  id: 27706,
  entitlementType: EntitlementType['HOUSING'],
  entitlementDetails: 'Account Metal application',
};

export const sampleWithNewData: NewBeneficiary = {
  entitlementType: EntitlementType['PENSION'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
