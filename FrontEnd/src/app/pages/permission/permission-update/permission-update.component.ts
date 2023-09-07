import { Component } from '@angular/core';
import { AbstractControl, ControlConfig, FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { RoleService } from '../role.service';
import { IRole } from '../../model/roles.model';
import { IPermission, Permission } from '../../model/permissionModel';
import { PermissionService } from '../permission.service';

@Component({
  selector: 'app-permission-update',
  templateUrl: './permission-update.component.html',
  styleUrls: ['./permission-update.component.css']
})
export class PermissionUpdateComponent {
  id?: number;
  roles: IRole[] = []
  permissions: any[] = [];
  rolesPermissionForm!: FormGroup;
  selectedPermissions: any = []
  selectedRole:any;

  queryParamRole?:String

  constructor(
    protected fb: FormBuilder,
    private permissionService: PermissionService,
    private rolesService: RoleService,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {
  }

  ngOnInit() {
    this.rolesPermissionForm = this.fb.group({
      role: ['', [Validators.required]],
      permissionCheckboxes: this.fb.array([])
    });

    this.getAllPermissions();
    this.getAllRoles()


  this.route.queryParams.subscribe(params => {
    this.queryParamRole = params['id'];
   
    if (this.queryParamRole != null) {
      // Set the initial value of the 'role' form control
      this.selectedRole = this.queryParamRole;
     this.onRoleChange();
    }
  });
  }

  getAllRoles() {
    this.rolesService.getRoles().subscribe(res => {
  

      if (res && res.body) {
        this.roles = res.body;
      }
    });
  }

  getAllPermissions() {
    this.permissionService.getPermissions().subscribe(res => {
      if (res && res.body) {
      
        this.permissions = res.body;
        this.permissions =this.permissions.map((d:any)=>{
           return{
            id:d.id,
            name:d.name,
            value:false
           }
        });
        
       // this.initializePermissionCheckboxes();
      }
    });
  }

  initializePermissionCheckboxes() {
    const permissionCheckboxesArray = this.rolesPermissionForm.get('permissionCheckboxes') as FormArray;

    // Remove all form controls from the FormArray
    while (permissionCheckboxesArray.length) {
      permissionCheckboxesArray.removeAt(0);
    }

    // Add new form controls based on permissions
    this.permissions.forEach(permission => {
      permissionCheckboxesArray.push(this.fb.control(false)); // Initialize as unchecked
    });
  }

  onSubmit() {
     const permissionOfRole ={
      id:this.selectedRole,
      permissions:this.permissions,
    }
    this.permissionService.updatePermissionOfRoles(permissionOfRole).subscribe((res)=>{
      this.toastr.success("succesfully changed the permissions")
    },error =>{
      this.toastr.error("there was a problem in changing the permissions")
    })
    window.location.reload()
  }

  findSelectedRole(roleName: string): IRole | undefined {
    return this.roles.find(role => role.name === roleName);
  }


    addPermission(obj:any){
    let perm = this.permissions.find(p=> p.id == obj.id)
    if(perm){
      perm.value = !obj.value
    }

   
  }

  onRoleChange() {
    

    // Find the selected role object
   
   this.permissions=  this.permissions.map(d=>{
     return {...d,
      value:false
     }
    })
    this.permissionService.getPermissionOfRoles(this.selectedRole).subscribe((res: any) => {
      console.log(res);
      // Iterate through the permissions array in the response
      res.permissions.forEach((perm: any) => {
        const permission = this.permissions.find((p: any) => p.id === perm.id);
        if (permission) {
          permission.value = true;
        }
      });
    
 
    });
  }


}