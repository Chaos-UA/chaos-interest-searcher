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

  filter: DashboardFilter = {
    fullTextSearch: '',
    interests: [],
    jobs: [],
    names: []
  };

  users: User[] = [];

  constructor(private apiService: ApiService) {

  }

  ngOnInit() {
    this.apiService.getInterests().subscribe((interests : SimpleItem[]) => {
      this.interests = interests;
    });

    this.apiService.getJobs().subscribe((jobs : SimpleItem[]) => {
      this.jobs = jobs;
    });

    this.apiService.getNames().subscribe((names : SimpleItem[]) => {
      this.names = names;
    });

    this.applyFilter();
  }

  applyFilter() {
    const params : SearchUserParams = {
      fullTextSearch: this.filter.fullTextSearch,
      interests: this.filter.interests.map(v => v.name),
      jobs: this.filter.jobs.map(v => v.name),
      names: this.filter.names.map(v => v.name)
    };

    this.apiService.searchUsers(params).subscribe((users : User[]) => {
      this.users = users;
    });
  }

  itemToString(interest: SimpleItem) {
    return `${interest.name} - ${interest.usersCount}`;
  }

  toPrettyJson(object: any) {
    return JSON.stringify(object);
  }
}

interface DashboardFilter {
  fullTextSearch: string;
  interests: SimpleItem[];
  jobs: SimpleItem[];
  names: SimpleItem[];
}
