import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {AngularMaterialModule} from "../angular-material.module";
import { DataSyncComponent } from './components/data-sync/data-sync.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import {FlexLayoutModule} from "@angular/flex-layout";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {ChaosSelectComponent} from "./components/chaos-select/chaos-select.component";
import {MatCheckboxModule} from "@angular/material/checkbox";

@NgModule({
  declarations: [
    AppComponent,
    DataSyncComponent,
    DashboardComponent,
    ChaosSelectComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        AngularMaterialModule,
        FlexLayoutModule,
        HttpClientModule,
        MatAutocompleteModule,
        FormsModule,
        ReactiveFormsModule,
        MatCheckboxModule,
        // ReactiveFormsModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
