import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AnnualDeclarationComponent } from './list/annual-declaration.component';
import { AnnualDeclarationDetailComponent } from './detail/annual-declaration-detail.component';
import { AnnualDeclarationUpdateComponent } from './update/annual-declaration-update.component';
import { AnnualDeclarationDeleteDialogComponent } from './delete/annual-declaration-delete-dialog.component';
import { AnnualDeclarationRoutingModule } from './route/annual-declaration-routing.module';

@NgModule({
  imports: [SharedModule, AnnualDeclarationRoutingModule],
  declarations: [
    AnnualDeclarationComponent,
    AnnualDeclarationDetailComponent,
    AnnualDeclarationUpdateComponent,
    AnnualDeclarationDeleteDialogComponent,
  ],
})
export class AnnualDeclarationModule {}
