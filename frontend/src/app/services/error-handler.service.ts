import {ErrorHandler, Injectable} from '@angular/core';
import {Router} from "@angular/router";
import {NotificationService} from "./notification.service";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler
{

    constructor(private router: Router,
                private notificationService: NotificationService)
    {
    }

    handleError(httpErrorObj)
    {
        console.error(httpErrorObj);
        if (httpErrorObj != null && httpErrorObj.error != null &&
            httpErrorObj.error.hasOwnProperty("name") && httpErrorObj.error.hasOwnProperty("message"))
        {
            this.notificationService.error(httpErrorObj.error.message, httpErrorObj.error.name);
        }
        else
        {
            this.notificationService.error("An unknown error has occurred", "Unknown Error");
        }
    }

    notFound()
    {
        this.notificationService.warning("The requested page was not found.");
        this.router.navigate(['/home']);
    }

    getErrorMessage(httpErrorObj): string
    {
        if (httpErrorObj != null && httpErrorObj.error != null)
        {
            let json = JSON.parse(httpErrorObj.error);
            return json.message;
        }
        else
        {
            return "An unknown error has occurred"
        }
    }

}
