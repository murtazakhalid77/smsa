import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { UserService, EntityUserResponseType } from '../service/user.service';
import { IUser, User } from 'src/app/pages/model/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, map } from 'rxjs';
@Component({
  selector: 'app-user-update',
  templateUrl: './user-update.component.html',
  styleUrls: ['./user-update.component.css']
})
export class UserUpdateComponent {
  userForm!: FormGroup
  user?: IUser;
  id: any;

  constructor(
    private formbuilder: FormBuilder, 
    private userService: UserService, 
    private router: Router,
    private route: ActivatedRoute,
    ) { }

  ngOnInit(): void {

    this.userForm = this.formbuilder.group({
      id: [''],
      name: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(7)]],
      status: ['', [Validators.required]],
    })

    this.route.queryParams.subscribe( params => {
      this.id = params['id'];
      if(this.id!=null){
        this.updateForm(this.id);
      }
    });
  }

  updateForm(id: any){

    this.getUserById(id).subscribe(res =>{
      if(res){
        debugger;
        this.user = res;
        this.userForm.patchValue(this.user);
        this.userForm.get('id')?.disable()
        this.userForm.get('password')?.clearValidators();
        this.userForm.get('password')?.updateValueAndValidity();
      }
    })
  }

  getUserById(id?: any): Observable<User | null> {
    return this.userService.getUserById(id).pipe(
      map((res: EntityUserResponseType) => {
        if (res && res.body) {
          return res.body;
        }
        return null;
      })
    );
  }

  submit(userForm: FormGroup){
    debugger;

    let user = {
      id: this.id,
      name: userForm.value.name,
      password: userForm.value.password,
      status: userForm.value.status
    }

    if(this.id!=null ){
      this.updateUser(user);
    }else{
      this.createUser(user);
    }
  }

  updateUser(user: User) {
    this.userService.updateUser(user).subscribe((res: any)=>{
      if(res){
        this.router.navigateByUrl('/user/view')
      }
    })
  }

  createUser(user: User){
    this.userService.addUser(user).subscribe((res: any)=>{
      if(res){
        this.router.navigateByUrl('/user/view')
      }
    })
  }

}
