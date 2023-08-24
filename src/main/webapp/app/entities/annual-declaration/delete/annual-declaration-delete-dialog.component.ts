import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnualDeclaration } from '../annual-declaration.model';
import { AnnualDeclarationService } from '../service/annual-declaration.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './annual-declaration-delete-dialog.component.html',
})
export class AnnualDeclarationDeleteDialogComponent {
  annualDeclaration?: IAnnualDeclaration;

  constructor(protected annualDeclarationService: AnnualDeclarationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.annualDeclarationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
