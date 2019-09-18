import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) { }

  public initiateSync() {
    this.httpClient.get("/api/users/initiate-sync").subscribe();
  }

  public getInterests() : Observable<SimpleItem[]> {
    return this.httpClient.get<SimpleItem[]>("/api/interests");
  }

  public getJobs() : Observable<SimpleItem[]> {
    return this.httpClient.get<SimpleItem[]>("/api/jobs");
  }

  public getNames() : Observable<SimpleItem[]> {
    return this.httpClient.get<SimpleItem[]>("/api/names");
  }

  public searchUsers(params : SearchUserParams) : Observable<User[]> {
    return this.httpClient.post<User[]>("/api/users", params);
  }

}

export interface User {
  id: string;
  name: string;
  age: bigint;
  largePhotoUrls: string[];
  newbie: string;
  hot: boolean;
  allowChat: boolean;
  allowQuickChat: boolean;
  onlineStatus: bigint;
  popularityLevel: bigint;
  popularityPnbPlace: bigint;
  popularityVisitorsToday: bigint;
  popularityVisitorsMonth: bigint;
  interests: string[];
  socialNetworks: SocialNetwork[];
  displayedAboutMe: string[];
  profileFields: any;
}
export interface SocialNetwork {
  networkType: string;
  networkUrl: string;
}

export interface SearchUserParams {
  fullTextSearch? : string;
  interests?: string[];
  jobs?: string[];
  names?: string[];
}

export interface SimpleItem {
  name: string;
  usersCount: bigint;
}
