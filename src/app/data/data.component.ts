import { AfterViewInit, Component, ViewChild, Inject } from '@angular/core';
import {
  MatPaginator,
  MatSort,
  MatTableDataSource,
  MatSnackBar
} from '@angular/material';
import {
  MatDialog,
  MatDialogRef,
  MatDialogConfig,
  MAT_DIALOG_DATA
} from '@angular/material';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http/src/response';
import { DataFormComponent } from '../data-form/data-form.component';
import { UserConfirmationComponent } from '../user-confirmation/user-confirmation.component';
@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css']
})
export class DataComponent implements AfterViewInit {
  displayedColumns = ['source', 'title', 'category', 'action'];
  dataSource = new MatTableDataSource();
  isLoadingResults = true;
  categorySelected = 'ALL';
  sourceSelected = 'ALL';
  condition = [];
  metaCategory = {
    text: 'ALL',
    type: 'TAG'
  };
  metaSource = {
    text: 'ALL',
    type: 'SRC'
  };
  userConfirmation = false;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private http: HttpClient,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));
    this.getData(this.paginator.pageIndex);
  }

  getData(pageIndex: number) {
    this.http
      .post<UserApi>('https://service.pixorr.com/data/getHome', {
        limit: 30,
        skip: 30 * pageIndex
      })
      .subscribe(
        data => {
          this.dataSource.data = data.message;
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

  setData(event) {
    this.isLoadingResults = true;
    if (this.categorySelected === 'ALL' && this.sourceSelected === 'ALL') {
      this.getData(event.pageIndex);
    } else {
      this.getFilteredData(event.pageIndex);
    }
  }

  changeDataSourceName(source) {
    switch (source) {
      case 'FPK':
        return 'Freepik';
      case 'FBC':
        return 'FreeBCard';
      case 'FRB':
        return 'Freeebbble';
      case 'GPB':
        return 'Graphberry';
      case 'GPF':
        return 'GraphicFuel';
      case 'ICT':
        return 'Iconstore';
      case 'FIT':
        return 'Flaticon';
      case 'MKW':
        return 'Mockupworld';
      case 'PXD':
        return 'Pixaden';
      case 'THJ':
        return 'The hungry jpeg';
      case 'TUP':
        return 'Tutpad';
      case 'TPL':
        return 'Tutplus';
      case 'WGH':
        return 'We Graphics';
      case 'PXB':
        return 'Pixabay';
      case 'PTD':
        return 'Photodune';
      case 'VDH':
        return 'VideoHive';
      case '3DO':
        return '3dOcean';
      case 'PXL':
        return 'Pexels';
    }
  }

  changeDataSourceCategory(category) {
    switch (category) {
      case 'FOT':
        return 'Photos';
      case 'ICN':
        return 'Icons';
      case 'VCT':
        return 'Vector';
      case 'PSD':
        return 'PSD';
      case 'UIK':
        return 'UIKit';
      case 'SKT':
        return 'Sketch';
      case 'MCK':
        return 'Mockup';
      case 'WLP':
        return 'Wallpaper';
      case 'BCK':
        return 'Background';
      case 'TXT':
        return 'Texture';
      case 'FNT':
        return 'Font';
      case 'TUT':
        return 'Tutorial';
      case 'YTB':
        return 'Youtube';
      case 'PXV':
        return 'PixaBay Videos';
    }
  }

  onSourceChanged(event) {
    this.isLoadingResults = true;
    switch (event.value) {
      case 'ALL':
        if (this.categorySelected === 'ALL') {
          this.getData(0);
        } else {
          this.getFilteredData(0);
        }
        break;
      default:
        this.setSourceSelectedData(event.value);
        this.getFilteredData(0);
        break;
    }
  }

  onCategoryChanged(event) {
    this.isLoadingResults = true;
    switch (event.value) {
      case 'ALL':
        if (this.sourceSelected === 'ALL') {
          this.getData(0);
        } else {
          this.getFilteredData(0);
        }
        break;
      default:
        this.setCategorySelectedData(event.value);
        this.getFilteredData(0);
        break;
    }
  }

  setSourceSelectedData(text: string) {
    if (this.metaCategory.text === 'ALL') {
      this.condition = [];
      this.metaSource.text = text;
      this.condition.push(this.metaSource);
    } else {
      if (this.condition.length === 2) {
        this.condition.splice(1, 1);
        this.metaSource.text = text;
        this.condition.push(this.metaSource);
      } else if (this.condition.length === 1) {
        this.metaSource.text = text;
        this.condition.push(this.metaSource);
      }
    }
  }

  setCategorySelectedData(text: string) {
    if (this.metaSource.text === 'ALL') {
      this.condition = [];
      this.metaCategory.text = text;
      this.condition.push(this.metaCategory);
    } else {
      if (this.condition.length === 2) {
        this.condition.splice(1, 1);
        this.metaCategory.text = text;
        this.condition.push(this.metaCategory);
      } else if (this.condition.length === 1) {
        this.metaCategory.text = text;
        this.condition.push(this.metaCategory);
      }
    }
  }

  getFilteredData(pageIndex: number) {
    this.http
      .post<UserApi>('https://service.pixorr.com/data/getFiltered', {
        limit: 30,
        skip: 30 * pageIndex,
        condition: this.condition
      })
      .subscribe(
        data => {
          this.dataSource.data = data.message;
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

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  openAddDialog() {
    const event = {
      pageIndex: 0
    };
    const dialogRef = this.dialog.open(DataFormComponent, {
      width: '70%',
      height: '70%',
      data: {}
    });
    dialogRef.afterClosed().subscribe(result => {
      this.setData(event);
    });
  }

  removeData(row) {
    const dialogRef = this.dialog.open(UserConfirmationComponent, {
      width: '400px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'yes') {
        this.userConfirmation = true;
        this.sendRequest(row._id);
      } else {
        this.userConfirmation = false;
      }
    });
  }

  sendRequest(id: string) {
    const event = {
      pageIndex: 0
    };
    this.http
      .post<UserApi>('https://service.pixorr.com/data/deleteData', {
        id: id
      })
      .subscribe(
        response => {
          if (response.status === true) {
            if (response.message == null) {
              this.snackBar.open("data doesn't exist in database", 'Okay', {
                duration: 2000
              });
            } else {
              this.snackBar.open('data deleted from the database', 'Okay', {
                duration: 2000
              });
            }
          } else {
            this.snackBar.open('data not deleted', 'Okay', {
              duration: 2000
            });
          }
          this.setData(event);
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
}
interface UserApi {
  status: boolean;
  message: Object[];
}
