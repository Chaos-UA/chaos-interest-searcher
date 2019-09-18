import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../service/api.service";

@Component({
  selector: 'app-data-sync',
  templateUrl: './data-sync.component.html',
  styleUrls: ['./data-sync.component.scss']
})
export class DataSyncComponent implements OnInit {

  constructor(private apiService: ApiService) { }

  ngOnInit() {

  }

  public initiateUsersSync() : void {
    this.apiService.initiateSync();
  }

}
