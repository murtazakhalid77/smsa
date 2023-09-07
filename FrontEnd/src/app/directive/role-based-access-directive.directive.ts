import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';

@Input()
@Directive({
  selector: '[appRoleBasedAccessDirective]'
})
export class RoleBasedAccessDirectiveDirective {

  @Input() set appRoleBasedAccess(allowedPermissions: string[]) {
  
    if (this.checkUserRole(allowedPermissions)) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef
  ) {}

  private checkUserRole(allowedPermissions: string[]): boolean {
    const userRoles= sessionStorage.getItem('role');
    const permissions= sessionStorage.getItem('permissions')
    
    if (userRoles && permissions) {
      // Parse the user's roles (in this example, it's a comma-separated string)
      const permission1 = permissions.split(',');
      
  
      return permission1.some(permissions => allowedPermissions.includes(permissions.trim()));
    }
    
    // If user roles are not available, deny access
    return false;
  }

}
