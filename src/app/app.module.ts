import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { DataComponent } from './data/data.component';
import { CdkTableModule } from '@angular/cdk/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatAutocompleteModule,
  MatButtonToggleModule,
  MatCardModule,
  MatChipsModule,
  MatDatepickerModule,
  MatDialogModule,
  MatExpansionModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatRippleModule,
  MatSelectModule,
  MatSidenavModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatSortModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTooltipModule,
  MatStepperModule
} from '@angular/material';
import { ManageComponent } from './manage/manage.component';
import { DataFormComponent } from './data-form/data-form.component';
import { LoginComponent } from './login/login.component';
import { UserConfirmationComponent } from './user-confirmation/user-confirmation.component';
import { AuthGuard } from './guard/auth.guard';
const appRoutes: Routes = [
  { path: '', component: DataComponent, canActivate: [AuthGuard] },
  { path: 'data', component: DataComponent, canActivate: [AuthGuard] },
  { path: 'manage', component: ManageComponent, canActivate: [AuthGuard] },
  { path: 'data-form', component: DataFormComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'app', component: AppComponent, canActivate: [AuthGuard] }
];
@NgModule({
  declarations: [
    AppComponent,
    DataComponent,
    ManageComponent,
    DataFormComponent,
    LoginComponent,
    UserConfirmationComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    MatDialogModule,
    MatButtonModule,
    MatCheckboxModule,
    CdkTableModule,
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatStepperModule,
    MatDatepickerModule,
    MatDialogModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  entryComponents: [DataFormComponent, UserConfirmationComponent],
  providers: [AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule {}
//platformBrowserDynamic().bootstrapModule(AppModule);
