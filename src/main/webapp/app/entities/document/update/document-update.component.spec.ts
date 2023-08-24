import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DocumentFormService } from './document-form.service';
import { DocumentService } from '../service/document.service';
import { IDocument } from '../document.model';
import { IAnnualDeclaration } from 'app/entities/annual-declaration/annual-declaration.model';
import { AnnualDeclarationService } from 'app/entities/annual-declaration/service/annual-declaration.service';

import { DocumentUpdateComponent } from './document-update.component';

describe('Document Management Update Component', () => {
  let comp: DocumentUpdateComponent;
  let fixture: ComponentFixture<DocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentFormService: DocumentFormService;
  let documentService: DocumentService;
  let annualDeclarationService: AnnualDeclarationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DocumentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentFormService = TestBed.inject(DocumentFormService);
    documentService = TestBed.inject(DocumentService);
    annualDeclarationService = TestBed.inject(AnnualDeclarationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AnnualDeclaration query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const annualDeclaration: IAnnualDeclaration = { id: 17105 };
      document.annualDeclaration = annualDeclaration;

      const annualDeclarationCollection: IAnnualDeclaration[] = [{ id: 47760 }];
      jest.spyOn(annualDeclarationService, 'query').mockReturnValue(of(new HttpResponse({ body: annualDeclarationCollection })));
      const additionalAnnualDeclarations = [annualDeclaration];
      const expectedCollection: IAnnualDeclaration[] = [...additionalAnnualDeclarations, ...annualDeclarationCollection];
      jest.spyOn(annualDeclarationService, 'addAnnualDeclarationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(annualDeclarationService.query).toHaveBeenCalled();
      expect(annualDeclarationService.addAnnualDeclarationToCollectionIfMissing).toHaveBeenCalledWith(
        annualDeclarationCollection,
        ...additionalAnnualDeclarations.map(expect.objectContaining)
      );
      expect(comp.annualDeclarationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const document: IDocument = { id: 456 };
      const annualDeclaration: IAnnualDeclaration = { id: 71747 };
      document.annualDeclaration = annualDeclaration;

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(comp.annualDeclarationsSharedCollection).toContain(annualDeclaration);
      expect(comp.document).toEqual(document);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 123 };
      jest.spyOn(documentFormService, 'getDocument').mockReturnValue(document);
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentFormService.getDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentService.update).toHaveBeenCalledWith(expect.objectContaining(document));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 123 };
      jest.spyOn(documentFormService, 'getDocument').mockReturnValue({ id: null });
      jest.spyOn(documentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentFormService.getDocument).toHaveBeenCalled();
      expect(documentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 123 };
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAnnualDeclaration', () => {
      it('Should forward to annualDeclarationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(annualDeclarationService, 'compareAnnualDeclaration');
        comp.compareAnnualDeclaration(entity, entity2);
        expect(annualDeclarationService.compareAnnualDeclaration).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
