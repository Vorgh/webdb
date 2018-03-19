import {ErrorHandler, Injectable} from '@angular/core';
import {Router} from "@angular/router";
import {NotificationService} from "./notification.service";
import {GeneralError} from "../models/rest/rest-errors";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler
{

    constructor(private router: Router,
                private notificationService: NotificationService)
    {
    }

    handleError(httpErrorObj)
    {
        console.log(httpErrorObj);
        if (httpErrorObj.error.hasOwnProperty("name") && httpErrorObj.error.hasOwnProperty("message"))
        {
            //let generalError: GeneralError = new GeneralError(error.name, error.message);
            this.notificationService.error(httpErrorObj.error.name + ": " + httpErrorObj.error.message);
        }
        else
        {
            this.notificationService.error("Unknown error");
        }
    }

    notFound()
    {
        this.notificationService.warning("The requested page was not found.");
        this.router.navigate(['/home']);
    }

}
