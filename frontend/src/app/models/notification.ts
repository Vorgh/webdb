export class PushNotification
{
    title: string;
    message: string;
    type: "info" | "success" | "warning" | "danger" = "danger";
    date: Date;

    constructor(message: string, type, title="")
    {
        this.message = message;
        this.type = type;
        this.title = title;
        this.date = new Date();
    }
}
