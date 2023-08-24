import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocument, NewDocument } from '../document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocument for edit and NewDocumentFormGroupInput for create.
 */
type DocumentFormGroupInput = IDocument | PartialWithRequiredKeyOf<NewDocument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocument | NewDocument> = Omit<T, 'submissionDate'> & {
  submissionDate?: string | null;
};

type DocumentFormRawValue = FormValueOf<IDocument>;

type NewDocumentFormRawValue = FormValueOf<NewDocument>;

type DocumentFormDefaults = Pick<NewDocument, 'id' | 'submissionDate'>;

type DocumentFormGroupContent = {
  id: FormControl<DocumentFormRawValue['id'] | NewDocument['id']>;
  fileName: FormControl<DocumentFormRawValue['fileName']>;
  filePath: FormControl<DocumentFormRawValue['filePath']>;
  submissionDate: FormControl<DocumentFormRawValue['submissionDate']>;
  documentdType: FormControl<DocumentFormRawValue['documentdType']>;
  annualDeclaration: FormControl<DocumentFormRawValue['annualDeclaration']>;
};

export type DocumentFormGroup = FormGroup<DocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentFormService {
  createDocumentFormGroup(document: DocumentFormGroupInput = { id: null }): DocumentFormGroup {
    const documentRawValue = this.convertDocumentToDocumentRawValue({
      ...this.getFormDefaults(),
      ...document,
    });
    return new FormGroup<DocumentFormGroupContent>({
      id: new FormControl(
        { value: documentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fileName: new FormControl(documentRawValue.fileName, {
        validators: [Validators.required],
      }),
      filePath: new FormControl(documentRawValue.filePath, {
        validators: [Validators.required],
      }),
      submissionDate: new FormControl(documentRawValue.submissionDate, {
        validators: [Validators.required],
      }),
      documentdType: new FormControl(documentRawValue.documentdType),
      annualDeclaration: new FormControl(documentRawValue.annualDeclaration),
    });
  }

  getDocument(form: DocumentFormGroup): IDocument | NewDocument {
    return this.convertDocumentRawValueToDocument(form.getRawValue() as DocumentFormRawValue | NewDocumentFormRawValue);
  }

  resetForm(form: DocumentFormGroup, document: DocumentFormGroupInput): void {
    const documentRawValue = this.convertDocumentToDocumentRawValue({ ...this.getFormDefaults(), ...document });
    form.reset(
      {
        ...documentRawValue,
        id: { value: documentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DocumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      submissionDate: currentTime,
    };
  }

  private convertDocumentRawValueToDocument(rawDocument: DocumentFormRawValue | NewDocumentFormRawValue): IDocument | NewDocument {
    return {
      ...rawDocument,
      submissionDate: dayjs(rawDocument.submissionDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentToDocumentRawValue(
    document: IDocument | (Partial<NewDocument> & DocumentFormDefaults)
  ): DocumentFormRawValue | PartialWithRequiredKeyOf<NewDocumentFormRawValue> {
    return {
      ...document,
      submissionDate: document.submissionDate ? document.submissionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
