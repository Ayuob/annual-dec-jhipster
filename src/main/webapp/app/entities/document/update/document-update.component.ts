import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DocumentFormService, DocumentFormGroup } from './document-form.service';
import { IDocument } from '../document.model';
import { DocumentService } from '../service/document.service';
import { IAnnualDeclaration } from 'app/entities/annual-declaration/annual-declaration.model';
import { AnnualDeclarationService } from 'app/entities/annual-declaration/service/annual-declaration.service';
import { DocumentdType } from 'app/entities/enumerations/documentd-type.model';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;
  document: IDocument | null = null;
  documentdTypeValues = Object.keys(DocumentdType);

  annualDeclarationsSharedCollection: IAnnualDeclaration[] = [];

  editForm: DocumentFormGroup = this.documentFormService.createDocumentFormGroup();

  constructor(
    protected documentService: DocumentService,
    protected documentFormService: DocumentFormService,
    protected annualDeclarationService: AnnualDeclarationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAnnualDeclaration = (o1: IAnnualDeclaration | null, o2: IAnnualDeclaration | null): boolean =>
    this.annualDeclarationService.compareAnnualDeclaration(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => {
      this.document = document;
      if (document) {
        this.updateForm(document);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const document = this.documentFormService.getDocument(this.editForm);
    if (document.id !== null) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(document: IDocument): void {
    this.document = document;
    this.documentFormService.resetForm(this.editForm, document);

    this.annualDeclarationsSharedCollection = this.annualDeclarationService.addAnnualDeclarationToCollectionIfMissing<IAnnualDeclaration>(
      this.annualDeclarationsSharedCollection,
      document.annualDeclaration
    );
  }

  protected loadRelationshipsOptions(): void {
    this.annualDeclarationService
      .query()
      .pipe(map((res: HttpResponse<IAnnualDeclaration[]>) => res.body ?? []))
      .pipe(
        map((annualDeclarations: IAnnualDeclaration[]) =>
          this.annualDeclarationService.addAnnualDeclarationToCollectionIfMissing<IAnnualDeclaration>(
            annualDeclarations,
            this.document?.annualDeclaration
          )
        )
      )
      .subscribe((annualDeclarations: IAnnualDeclaration[]) => (this.annualDeclarationsSharedCollection = annualDeclarations));
  }
}
