import dayjs from 'dayjs/esm';

import { IFamilyMember, NewFamilyMember } from './family-member.model';

export const sampleWithRequiredData: IFamilyMember = {
  id: 49379,
  nationalNumber: 'calculating ',
  name: 'Gloves interface Granite',
  dateOfBirth: dayjs('2023-08-23'),
  gender: 'flexibility',
};

export const sampleWithPartialData: IFamilyMember = {
  id: 47101,
  nationalNumber: 'plum synergi',
  name: 'index',
  dateOfBirth: dayjs('2023-08-23'),
  gender: 'Loan',
};

export const sampleWithFullData: IFamilyMember = {
  id: 92815,
  nationalNumber: 'Wooden Accou',
  name: 'Group application',
  dateOfBirth: dayjs('2023-08-23'),
  gender: 'Montserrat Metal',
};

export const sampleWithNewData: NewFamilyMember = {
  nationalNumber: 'Rustic Baht ',
  name: 'Data methodology',
  dateOfBirth: dayjs('2023-08-23'),
  gender: 'infrastructure Director Account',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
