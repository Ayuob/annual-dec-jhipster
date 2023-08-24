import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SocialSecurityPensionerComponent } from './list/social-security-pensioner.component';
import { SocialSecurityPensionerDetailComponent } from './detail/social-security-pensioner-detail.component';
import { SocialSecurityPensionerUpdateComponent } from './update/social-security-pensioner-update.component';
import { SocialSecurityPensionerDeleteDialogComponent } from './delete/social-security-pensioner-delete-dialog.component';
import { SocialSecurityPensionerRoutingModule } from './route/social-security-pensioner-routing.module';

@NgModule({
  imports: [SharedModule, SocialSecurityPensionerRoutingModule],
  declarations: [
    SocialSecurityPensionerComponent,
    SocialSecurityPensionerDetailComponent,
    SocialSecurityPensionerUpdateComponent,
    SocialSecurityPensionerDeleteDialogComponent,
  ],
})
export class SocialSecurityPensionerModule {}
