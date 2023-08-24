import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BeneficiaryComponent } from './list/beneficiary.component';
import { BeneficiaryDetailComponent } from './detail/beneficiary-detail.component';
import { BeneficiaryUpdateComponent } from './update/beneficiary-update.component';
import { BeneficiaryDeleteDialogComponent } from './delete/beneficiary-delete-dialog.component';
import { BeneficiaryRoutingModule } from './route/beneficiary-routing.module';

@NgModule({
  imports: [SharedModule, BeneficiaryRoutingModule],
  declarations: [BeneficiaryComponent, BeneficiaryDetailComponent, BeneficiaryUpdateComponent, BeneficiaryDeleteDialogComponent],
})
export class BeneficiaryModule {}
