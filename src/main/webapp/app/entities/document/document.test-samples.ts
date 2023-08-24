import dayjs from 'dayjs/esm';

import { DocumentdType } from 'app/entities/enumerations/documentd-type.model';

import { IDocument, NewDocument } from './document.model';

export const sampleWithRequiredData: IDocument = {
  id: 70614,
  fileName: 'Planner Borders',
  filePath: 'Kids Home Berkshire',
  submissionDate: dayjs('2023-08-22T20:31'),
};

export const sampleWithPartialData: IDocument = {
  id: 36239,
  fileName: 'connect',
  filePath: 'Towels Cuban Cambridgeshire',
  submissionDate: dayjs('2023-08-22T18:49'),
};

export const sampleWithFullData: IDocument = {
  id: 96248,
  fileName: 'withdrawal repurpose',
  filePath: 'SMS Incredible Applications',
  submissionDate: dayjs('2023-08-22T20:17'),
  documentdType: DocumentdType['RESIDENCE'],
};

export const sampleWithNewData: NewDocument = {
  fileName: 'B2C mindshare neural',
  filePath: 'Brazil',
  submissionDate: dayjs('2023-08-23T00:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
