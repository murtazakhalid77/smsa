import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { OrdersComponent } from './pages/customer/orders.component';
import { LoginFormComponent } from './pages/login-form/login-form.component';
import { AddOrderComponent } from './pages/add-customer/add-order.component';
import { EditOrderComponent } from './pages/edit-customer/edit-order.component';
import { ImportComponent } from './pages/import/import.component';
// import { AuthGuard } from './auth.guard';
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
import { ExcelComponent } from './pages/importCustom/excel/excel.component';
import { ManifestDataComponent } from './pages/manifest-data/list/manifest-data.component';
import { ManifestDataUpdateComponent } from './pages/manifest-data/update/manifest-data-update.component';

export const routes: Routes = [
  {
    path: '',
    component: LoginFormComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
  },
  {
    path: 'customer/view',
    component: OrdersComponent,
  },
  {
    path: 'login',
    component: LoginFormComponent
  },
  {
    path: 'customer',
    component: AddOrderComponent,
  },
  {
    path: 'customer/:id',  // Define the route with the parameter ':id'
    component: AddOrderComponent,
    // canActivate: [AuthGuard],  // Optional: Add canActivate guard if needed
  },
  {
    path: 'import',
    component: ImportComponent,
  },
  {
    path: 'importCustomExcel',
    component: ExcelComponent,
  },
  {
    path: 'custom/view',
    component: CustomPortComponent,
  },
  {
    path: 'custom',
    component: CustomUpdateComponent,
  },
  {
    path: 'user/view',
    component: UserComponent,
  },
  {
    path: 'user',
    component: UserUpdateComponent,
  }
  ,
  {
    path: 'currency',
    component: CurrencyUpdateComponent,
  },
  {
    path: 'currency/view',
    component: CurrencyListComponent,
  },
  {
    path: 'currency/history',
    component: CurrencyHistoryComponent,
  },
  {
    path: 'region',
    component: RegionUpdateComponent,
  },
  {
    path: 'region/view',
    component: RegionListComponent,
  },
  {
    path: 'reports',
    component: ReportComponent,
  }
  ,
  {
    path: 'permissions',
    component: PermissionUpdateComponent,
  },
  {
    path: 'permissions/:role',
    component: PermissionUpdateComponent,
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
  },
  {
    path: 'manifestData',
    component: ManifestDataComponent
  },
  {
    path: 'manifestDataUpdate',
    component: ManifestDataUpdateComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
