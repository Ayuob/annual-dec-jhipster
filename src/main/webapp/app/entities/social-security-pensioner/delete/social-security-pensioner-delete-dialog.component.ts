import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISocialSecurityPensioner } from '../social-security-pensioner.model';
import { SocialSecurityPensionerService } from '../service/social-security-pensioner.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './social-security-pensioner-delete-dialog.component.html',
})
export class SocialSecurityPensionerDeleteDialogComponent {
  socialSecurityPensioner?: ISocialSecurityPensioner;

  constructor(protected socialSecurityPensionerService: SocialSecurityPensionerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.socialSecurityPensionerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
