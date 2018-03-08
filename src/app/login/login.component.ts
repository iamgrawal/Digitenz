import { Component, Directive } from '@angular/core';
import { FormsModule, Validators, FormControl } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import {AppComponent} from '../app.component';
import { MatSnackBar } from '@angular/material';
import { OnInit } from '@angular/core/src/metadata/lifecycle_hooks';
import { INVALID } from '@angular/forms/src/model';
@Component({
  selector: 'app-root',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  email = new FormControl('', [Validators.required, Validators.email]);
  password = new FormControl('', [Validators.required]);
  authenticate = false;
  hide = true;

  returnUrl: string;
  ngOnInit() {
    //localStorage.removeItem('currentUser');
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }
  getErrorMessage() {
    return this.email.hasError('required') ? 'You must enter a username' : this.email.hasError('email') ? 'Not a valid email' : '';
  }
  getpassErrMessage() {
    return this.password.hasError('required') ? 'You must enter a password' : '';
  }
  constructor( private http: HttpClient, private route: ActivatedRoute,
    private router: Router, private appComponent: AppComponent, private snackBar: MatSnackBar) {
      this.appComponent.loggedIn = null;
     }

  checkCredentials() {
    if (this.validateLogin()) {
      this.http.post<UserApi>('https://service.pixorr.com/emp/authentication', {
        email: this.email.value,
        password: this.password.value
      }).subscribe(response => {
        if (response.status) {
          sessionStorage.setItem('currentUser', response.messsage.name);
          this.appComponent.loggedIn = sessionStorage.getItem('currentUser');
          this.appComponent.isAdmin=response.messsage.isAdmin;
          this.router.navigate([this.returnUrl]);
        }else{
          this.snackBar.open('Email Password is not correct', '', {
            duration: 2000
      });    
        }
        response.status === true ? this.authenticate = true : this.authenticate = false;
      });
    }
  }

  validateLogin() {
    if (this.email.value === undefined || this.password.value === undefined || this.email.invalid || this.password.invalid ) {
      this.snackBar.open('Oops! You left something or some error your input', '', {
        duration: 2000
      });
      return false;
    } else {
      return true;
    }
  }
}

interface UserApi {
  status: boolean;
  messsage:{
    name:string,
    isAdmin:boolean
  },
}

