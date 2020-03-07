import { Component, OnInit } from '@angular/core';
import {ApiService, SearchUserParams, SimpleItem, User} from "../../service/api.service";
import {FormControl} from "@angular/forms";
import {startWith} from "rxjs/operators";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  interests: SimpleItem[] = [];
  jobs: SimpleItem[] = [];
  names: SimpleItem[] = [];
  ages: SimpleItem[] = [];

  filter: DashboardFilter = {
    allowQuickChat: false,
    fullTextSearch: '',
    interests: [],
    jobs: [],
    names: [],
    ages: []
  };

  users: User[] = [];

  constructor(private apiService: ApiService) {

  }

  ngOnInit() {
    this.reload();
  }

  reload() {
    const params: SearchUserParams = this.buildSearchUserParams();

    this.apiService.getInterests(params).subscribe((interests: SimpleItem[]) => {
      this.interests = interests;
    });

    this.apiService.getJobs(params).subscribe((jobs: SimpleItem[]) => {
      this.jobs = jobs;
    });

    this.apiService.getNames(params).subscribe((names: SimpleItem[]) => {
      this.names = names;
    });

    this.apiService.getAges(params).subscribe((ages: SimpleItem[]) => {
      this.ages = ages;
    });

    this.applyFilter();
  }

  applyFilter() {
    const params: SearchUserParams = this.buildSearchUserParams();

    this.apiService.searchUsers(params).subscribe((users: User[]) => {
      this.users = users;
    });
  }

  buildSearchUserParams(): SearchUserParams {
    const params: SearchUserParams = {
      allowQuickChat: this.filter.allowQuickChat,
      fullTextSearch: this.filter.fullTextSearch,
      interests: this.filter.interests.map(v => v.name),
      jobs: this.filter.jobs.map(v => v.name),
      names: this.filter.names.map(v => v.name),
      ages: this.filter.ages.map(v => v.name)
    };
    return params;
  }

  itemToString(interest: SimpleItem) {
    return `${interest.name} - ${interest.usersCount}`;
  }

  toPrettyJson(object: any) {
    return JSON.stringify(object);
  }
}

interface DashboardFilter {
  allowQuickChat: boolean;
  fullTextSearch: string;
  interests: SimpleItem[];
  jobs: SimpleItem[];
  names: SimpleItem[];
  ages: SimpleItem[];
}
