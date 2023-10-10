import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EntityUsersResponseType, UserService } from '../service/user.service';
import { IUser } from '../../model/user.model';
import { environment } from 'src/Environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit{

  users?: IUser[];
  currentPage:number  = 0;
  itemsPerPage: number = 10;
  totalItems?: string;
  searchText?: string
  _url = environment.backend;

  constructor(private router: Router, private userService: UserService, private http: HttpClient) { }

  ngOnInit(): void {
    this.getUsers(this.currentPage, this.itemsPerPage);
  }

  getUsers(page?: number, size?: number) {
    let search = {};
  
    const pageable = {
      page: page,
      size: size,
    };
  
    if (this.searchText !== undefined && this.searchText !== '') {
      search = {
        mapper: 'USER',
        searchText: this.searchText,
      };
    }
  
    // Construct the query parameter for pageable
    const queryParams = {
      page: page,
      size: size,
      search: JSON.stringify(search),
    };
  
    this.userService.getUsers(queryParams).subscribe(
      (res: EntityUsersResponseType) => {
        if (res && res.body) {
          this.users = res.body;
          this.totalItems = res.headers.get('X-Total-Count') || '';
          console.log(this.users);
        }
      },
      (error) => {
        this.users = [];
        console.error('Error fetching customers:', error);
      }
    );
  }
  

  addUser(){
    this.router.navigateByUrl('/user')
  }

  updateUser(id?: any){
    this.router.navigate(['/user'], { queryParams: { id: id } });
  }

  changePage(value: any){
    this.getUsers(value.pageIndex, this.itemsPerPage);
  }

  downloadFile() {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http
      .get(`${this._url}/excel/user`, {
        responseType: 'blob', // Important: responseType should be 'blob'
        headers,
      })
      .subscribe((response: any) => {
        const blob = new Blob([response], { type: 'application/octet-stream' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'sales_report.xlsx'; // Set the desired filename here
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      });
  }

}
