import { Component, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'app-user-confirmation',
  templateUrl: './user-confirmation.component.html',
  styleUrls: ['./user-confirmation.component.css']
})
export class UserConfirmationComponent {

  constructor(public dialogRef: MatDialogRef < UserConfirmationComponent >,
    @Inject(MAT_DIALOG_DATA)public data: any) {}

  onNoClick(): void {
    this.dialogRef.close('no');
  }

  onYesClick(): void {
    this.dialogRef.close('yes');
  }
}
