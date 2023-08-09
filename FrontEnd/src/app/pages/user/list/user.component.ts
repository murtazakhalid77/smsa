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

  constructor(private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers() {
    this.userService.getUsers().subscribe(
      (res: EntityUsersResponseType) => {
        if(res && res.body){
          this.users = res.body;
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


}
