import { Component, Inject } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSnackBar
} from '@angular/material';
import { MatChipInputEvent } from '@angular/material';
import { ENTER, COMMA, SPACE } from '@angular/cdk/keycodes';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormControl, Validators } from '@angular/forms';
import { VALID } from '@angular/forms/src/model';

@Component({
  selector: 'app-data-form',
  templateUrl: './data-form.component.html',
  styleUrls: ['./data-form.component.css']
})
export class DataFormComponent {
  visible = true;
  selectable = true;
  addOnBlur = true;
  removable = true;
  gstNo = new FormControl('', [Validators.required]);
  seller = new FormControl('', [Validators.required]);
  description = new FormControl('', [Validators.required]);
  name = new FormControl('', [Validators.required]);
  billDate = new FormControl('', [Validators.required]);
  amount = new FormControl('', [Validators.required]);
  button: string;

  addEntry = {
    gstNo: null,
    seller: null,
    description: null,
    name: null,
    billDate: null,
    amount: null
  };

  constructor(
    public dialogRef: MatDialogRef<DataFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private http: HttpClient,
    public snackBar: MatSnackBar
  ) {}

  getGstErrorMessage() {
    return this.gstNo.hasError('required') ? 'You must enter a value' : '';
  }
  getSellerErrorMessage() {
    return this.seller.hasError('required') ? 'You must enter a value' : '';
  }
  getDescriptionErrMessage() {
    return this.description.hasError('required')
      ? 'You must enter description.'
      : '';
  }
  getNameErrMessage() {
    return this.name.hasError('required') ? 'You must enter the name.' : '';
  }
  getBillDateErrMessage() {
    return this.billDate.hasError('required')
      ? 'You must enter a bill date.'
      : '';
  }
  getAmountErrMessage() {
    return this.amount.hasError('required')
      ? 'You must enter a bill date.'
      : '';
  }

  onFormSubmitButton() {
    if (this.validate()) {
      this.addEntry = {
        gstNo: this.gstNo.value,
        seller: this.seller.value,
        amount: this.amount.value,
        description: this.description.value,
        name: this.name.value,
        billDate: this.billDate
      };
      this.http.post<UserApi>('', this.addEntry).subscribe(response => {
        if (response.status === true) {
          this.snackBar.open('data added to the database', 'Okay', {
            duration: 2000
          });
        } else {
          this.snackBar.open('data not added', 'Okay', {
            duration: 2000
          });
        }
        this.dialogRef.close();
      });
    }
  }

  onFormCancelButton() {
    this.addEntry = {
      gstNo: null,
      seller: null,
      description: null,
      name: null,
      billDate: null,
      amount: null
    };
    this.dialogRef.close();
  }

  validate() {
    if (
      this.gstNo.value === undefined ||
      this.gstNo.invalid ||
      this.seller.value === undefined ||
      this.seller.invalid ||
      this.amount.value === undefined ||
      this.amount.invalid ||
      this.description.value === undefined ||
      this.description.invalid ||
      this.name.value === undefined ||
      this.name.invalid ||
      this.billDate.value === undefined ||
      this.billDate.invalid
    ) {
      this.snackBar.open(
        'Oops! You left something or some error in your input',
        '',
        {
          duration: 2000
        }
      );
      return false;
    } else {
      return true;
    }
  }
}

interface UserApi {
  status: boolean;
  message: Object[];
}
