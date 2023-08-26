import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBeneficiary } from '../beneficiary.model';
import { BeneficiaryService } from '../service/beneficiary.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './beneficiary-delete-dialog.component.html',
})
export class BeneficiaryDeleteDialogComponent {
  beneficiary?: IBeneficiary;

  constructor(protected beneficiaryService: BeneficiaryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(familyMembersId: number, annualDeclarationId: number): void {
    this.beneficiaryService.delete(familyMembersId, annualDeclarationId).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
