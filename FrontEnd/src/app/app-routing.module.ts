import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { OrdersComponent } from './pages/customer/orders.component';
import { LoginFormComponent } from './pages/login-form/login-form.component';
import { AddOrderComponent } from './pages/add-customer/add-order.component';
import { EditOrderComponent } from './pages/edit-customer/edit-order.component';
import { ImportComponent } from './pages/import/import.component';
import { AuthGuard } from './auth.guard';
import { CustomPortComponent } from './pages/custom-port/list/custom-port.component';
import { CustomUpdateComponent } from './pages/custom-port/update/custom-update.component';
import { UserComponent } from './pages/user/list/user.component';
import { UserUpdateComponent } from './pages/user/update/user-update.component';

const routes: Routes = [
  {
    path: '',
    component: LoginFormComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'customer/view',
    component: OrdersComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'login',
    component: LoginFormComponent
  },
  {
    path: 'customer',
    component: AddOrderComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'customer/:id',
    component: EditOrderComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'import',
    component: ImportComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'custom/view',
    component: CustomPortComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'custom',
    component: CustomUpdateComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user/view',
    component: UserComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'user',
    component: UserUpdateComponent,
    canActivate: [AuthGuard],
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
