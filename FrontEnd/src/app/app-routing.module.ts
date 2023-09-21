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
import { CurrencyUpdateComponent } from './pages/currency/update/currency-update.component';
import { CurrencyListComponent } from './pages/currency/list/currency-list.component';
import { RegionUpdateComponent } from './pages/region/update/region-update.component';
import { RegionListComponent } from './pages/region/list/region-list.component';
import { ReportComponent } from './pages/report/report.component';
import { PermissionUpdateComponent } from './pages/permission/permission-update/permission-update.component';
import { UnauthorizedComponent } from './pages/unauthorized/unauthorized.component';
import { CurrencyHistoryComponent } from './pages/currency/currency-history/currency-history.component';
import { SheetHistoryListComponent } from './pages/sheetHistory/sheet-history-list/sheet-history-list.component';
import { TransactionComponent } from './pages/tran/transaction/transaction.component';

export const routes: Routes = [
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
    path: 'customer/:id',  // Define the route with the parameter ':id'
    component: AddOrderComponent,
    canActivate: [AuthGuard],  // Optional: Add canActivate guard if needed
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
  ,
  {
    path: 'currency',
    component: CurrencyUpdateComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'currency/view',
    component: CurrencyListComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'currency/history',
    component: CurrencyHistoryComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'region',
    component: RegionUpdateComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'region/view',
    component: RegionListComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'reports',
    component: ReportComponent,
    canActivate: [AuthGuard],
  }
  ,
  {
    path: 'permissions',
    component: PermissionUpdateComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'permissions/:role',
    component: PermissionUpdateComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent,
  },
  {
    path: 'sheetHistory',
    component: SheetHistoryListComponent,
  }
  ,{
    path: 'transactions',
    component: TransactionComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
