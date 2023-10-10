
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { provideRouter, withHashLocation } from '@angular/router';
import { AppComponent } from './app.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { DashboardHeadComponent } from './components/dashboard-head/dashboard-head.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { OrdersComponent } from './pages/customer/orders.component';
import { LoginFormComponent } from './pages/login-form/login-form.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddOrderComponent } from './pages/add-customer/add-order.component';
import { EditOrderComponent } from './pages/edit-customer/edit-order.component';
import { ImportComponent } from './pages/import/import.component';
import { AuthGuard } from './auth.guard';
import { AuthInterceptor } from './services/interceptors/auth.interceptor';
import { CustomPortComponent } from './pages/custom-port/list/custom-port.component';
import { CustomUpdateComponent } from './pages/custom-port/update/custom-update.component';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule, DatePipe } from '@angular/common';
import { LoaderComponent } from './pages/loader/loader.component';
import { LoaderInterceptor } from './services/interceptors/loader.interceptor';
import { UserComponent } from './pages/user/list/user.component';
import { UserUpdateComponent } from './pages/user/update/user-update.component';
import { CurrencyListComponent } from './pages/currency/list/currency-list.component';
import { CurrencyUpdateComponent } from './pages/currency/update/currency-update.component';
import { RegionListComponent } from './pages/region/list/region-list.component';
import { RegionUpdateComponent } from './pages/region/update/region-update.component';
import { ReportComponent } from './pages/report/report.component';
import { PermissionListComponent } from './pages/permission/permission-list/permission-list.component';
import { PermissionUpdateComponent } from './pages/permission/permission-update/permission-update.component';
import { UnauthorizedComponent } from './pages/unauthorized/unauthorized.component';
import { CurrencyHistoryComponent } from './pages/currency/currency-history/currency-history.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { MatPaginatorModule } from '@angular/material/paginator';
import { SheetHistoryListComponent } from './pages/sheetHistory/sheet-history-list/sheet-history-list.component';
import {routes} from './app-routing.module';
import { TransactionComponent } from './pages/tran/transaction/transaction.component';
import { DashboardCardsComponent } from './pages/dashboard-cards/dashboard-cards.component';
import { PieChartComponent } from './pages/pie-chart/pie-chart.component';
import { StackedBarChartComponent } from './pages/stacked-bar-chart/stacked-bar-chart.component'
import { NgModule } from '@angular/core';
import { AutoGrowDirective } from './directives/auto-grow-directive.directive';
@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    DashboardHeadComponent,
    DashboardComponent,
    OrdersComponent,
    LoginFormComponent,
    AddOrderComponent,
    EditOrderComponent,
    ImportComponent,
    CustomPortComponent,
    CustomUpdateComponent,
    LoaderComponent,
    UserUpdateComponent,
    UserComponent,
    CurrencyListComponent,
    CurrencyUpdateComponent,
    RegionListComponent,
    RegionUpdateComponent,
    ReportComponent,
    PermissionListComponent,
    PermissionUpdateComponent,
    UnauthorizedComponent,
    ReportComponent,
    CurrencyHistoryComponent,
    SheetHistoryListComponent,
    TransactionComponent,
    DashboardCardsComponent,
    PieChartComponent,
    StackedBarChartComponent,
    AutoGrowDirective
  ],
  imports: [
    NgxPaginationModule,
    BrowserModule,
    MatPaginatorModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 10000,
      progressBar: true,
      progressAnimation: 'increasing',
      preventDuplicates: true
    }),
  ],
  providers: [
    AuthGuard,
    DatePipe,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true }, // Register the AuthInterceptor
    provideRouter(routes, withHashLocation()),
],
  bootstrap: [AppComponent]
})
export class AppModule { }
