import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';
import { UserService, EntityUserResponseType } from '../service/user.service';
import { IUser, User } from 'src/app/pages/model/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, map } from 'rxjs';
import { RoleService } from '../../permission/role.service';
@Component({
  selector: 'app-user-update',
  templateUrl: './user-update.component.html',
  styleUrls: ['./user-update.component.css']
})
export class UserUpdateComponent {
  userForm!: FormGroup
  selectedRole: string = '';
  user?: IUser;
  id: any;
  selectedId?:any;
  roles: any[] = [];

  constructor(
    private formbuilder: FormBuilder, 
    private userService: UserService, 
    private router: Router,
    private route: ActivatedRoute,
    private rolesService:RoleService
    ) { }

  ngOnInit(): void {
    this.getAllRoles();

    this.userForm = this.formbuilder.group({
      id: [''],
      name: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(7)]],
      status: ['', [Validators.required]],
      role:['']
    })

    
    this.route.queryParams.subscribe( params => {
      this.id = params['id'];
      if(this.id!=null){
        this.updateForm(this.id);
      }
    });
  }

  changeRole() {
   
    // Get the selected role from the form control
    this.selectedId = this.roles.findIndex(role => role.name === this.selectedRole);
  
  }

  gotoRole(){
    this.router.navigate(['/permissions'], { queryParams: { id: this.selectedId+1 } });
  }
  updateForm(id: any){

    this.getUserById(id).subscribe(res =>{
      if (res) {
        const role = res.roles[0]?.name || '';
    
        const formData = {
          id: res.id || '', 
          name: res.name || '',
          password: '', 
          role: role,
          status: res.status || false,
        };
    
        this.userForm.patchValue(formData);
        this.userForm.get('id')?.disable();
        this.userForm.get('password')?.clearValidators();
        this.userForm.get('password')?.updateValueAndValidity();
      }
    })
  }

 

  getUserById(id?: any): Observable<any | null> {
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

    let user = {
      id: this.id,
      name: userForm.value.name,
      password: userForm.value.password,
      status: userForm.value.status,
      role:userForm.value.role
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

  getAllRoles() {
    this.rolesService.getRoles().subscribe(res => {

      if (res && res.body) {
        this.roles = res.body;
      }
    });
  }

}
