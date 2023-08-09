import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpEventType } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, tap } from "rxjs";
import { LoginService } from "../login.service";

@Injectable()
export class LoaderInterceptor implements HttpInterceptor {

  constructor(private loginService: LoginService){}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap((event) => {
        this.loginService.loader.next(true);
      }),
      // Handling the completion of the request, whether successful or not
      tap(
        (event) => {
          if (event.type === HttpEventType.Response) {
            this.loginService.loader.next(false);
          }
        },
        (error) => {
          // Handle errors if needed
          this.loginService.loader.next(false);
        }
      )
    );
  }
}