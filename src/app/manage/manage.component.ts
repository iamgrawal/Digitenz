import { Component, ViewChild } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MatDialogConfig,
  MAT_DIALOG_DATA,
  MatSnackBar,
  MatTableDataSource
} from '@angular/material';
import { AfterViewInit } from '@angular/core/src/metadata/lifecycle_hooks';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { UserConfirmationComponent } from '../user-confirmation/user-confirmation.component';

@Component({
  selector: 'app-manage',
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css']
})
export class ManageComponent implements AfterViewInit {
  displayedColumns = ['name', 'username', 'designation', 'action'];
  isLoadingResults = true;
  userConfirmation = false;
  dataSource = new MatTableDataSource();

  constructor(
    private http: HttpClient,
    private dialog: MatDialog,
    public snackBar: MatSnackBar
  ) {}

  ngAfterViewInit() {
    this.getData();
  }

  getData() {
    this.isLoadingResults = true;
    this.http
      .post<UserApi>(
        'http://4a843c51.ngrok.io:80/hacktivate/selectuser.php',
        {}
      )
      .subscribe(
        data => {
          this.dataSource.data = data.messsage;
          this.isLoadingResults = false;
        },
        (err: HttpErrorResponse) => {
          if (err.error instanceof Error) {
            alert(err.error.message);
          } else {
            alert(err.status);
          }
        }
      );
  }

  removeUser(id: string) {
    const dialogRef = this.dialog.open(UserConfirmationComponent, {
      width: '400px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'yes') {
        this.userConfirmation = true;
        this.sendRequest(id);
      } else {
        this.userConfirmation = false;
      }
    });
  }

  sendRequest(id: string) {
    this.http
      .post<UserApi>('http://4a843c51.ngrok.io:80/selectuser.php' /*TODO: */, {
        _id: id
      })
      .subscribe(response => {
        if (response.status === true) {
          this.snackBar.open('User Deleted', 'Okay', {
            duration: 2000
          });
          this.getData();
        } else {
          this.snackBar.open('User Not Deleted', 'Okay', {
            duration: 2000
          });
        }
      });
  }
}
interface UserApi {
  status: boolean;
  messsage: Object[];
}
