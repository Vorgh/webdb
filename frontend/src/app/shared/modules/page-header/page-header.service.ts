import {Injectable} from "@angular/core";
import {HeaderElement} from "../../../models/header-element";

@Injectable()
export class PageHeaderService
{
    root: HeaderElement;
    fragments: HeaderElement[] = [];

    constructor()
    {
        this.root = <HeaderElement>{id: 'home', parent: null, title: 'Home', link: '/home', icon: 'fa-dashboard'};
        this.fragments.push(this.root);
    }

    addFragment(header: HeaderElement)
    {
        let index = this.fragments.findIndex(elem => elem.id === header.id);
        if (index != -1)
            this.fragments[index] = header;
        else
            this.fragments.push(header);
    }

    getHeaderByID(id: string): HeaderElement
    {
        return this.fragments.find(elem => elem.id === id);
    }

    getPathFromHeader(current: HeaderElement): HeaderElement[]
    {
        let path: HeaderElement[] = [];

        while (current != null)
        {
            path.push(current);
            current = current.parent;
        }

        return path.reverse();
    }
}
