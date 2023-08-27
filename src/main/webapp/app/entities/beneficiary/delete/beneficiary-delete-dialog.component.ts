import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBeneficiary, BeneficiaryId } from '../beneficiary.model';
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

  confirmDelete(beneficiary: IBeneficiary): void {
    if (beneficiary.id.familyMemberId != null && beneficiary.id.annualDeclarationId != null)
      this.beneficiaryService.delete(beneficiary.id.familyMemberId, beneficiary.id.annualDeclarationId).subscribe(() => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      });
  }
}
