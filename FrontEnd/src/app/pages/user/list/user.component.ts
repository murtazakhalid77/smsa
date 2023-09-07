import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EntityUsersResponseType, UserService } from '../service/user.service';
import { IUser } from '../../model/user.model';

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

  constructor(private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    this.getUsers(this.currentPage, this.itemsPerPage);
  }

  getUsers(page?: any, size?: any) {
    this.userService.getUsers(page, size).subscribe(
      (res: EntityUsersResponseType) => {
        if(res && res.body){
          this.users = res.body;
          this.totalItems = res.headers.get('X-Total-Count') ?? '';
          console.log(this.users)
        } 
      },
      (error) => {
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

}
