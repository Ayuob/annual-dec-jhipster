import dayjs from 'dayjs/esm';
import { IAnnualDeclaration } from 'app/entities/annual-declaration/annual-declaration.model';
import { DocumentdType } from 'app/entities/enumerations/documentd-type.model';

export interface IDocument {
  id: number;
  fileName?: string | null;
  filePath?: string | null;
  submissionDate?: dayjs.Dayjs | null;
  documentdType?: DocumentdType | null;
  annualDeclaration?: Pick<IAnnualDeclaration, 'id' | 'submissionDate'> | null;
}

export type NewDocument = Omit<IDocument, 'id'> & { id: null };
