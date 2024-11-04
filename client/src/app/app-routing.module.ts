import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoanListComponent } from './loan/loan-list/loan-list.component';
import { CategoryListComponent } from './category/category-list/category-list.component';
import { GameListComponent } from './game/game-list/game-list.component';
import { CustomerListComponent } from './customer/customer-list/customer-list.component';
import { AuthorListComponent } from './author/author-list/author-list.component';

const routes: Routes = [
    { path: '', redirectTo: '/games', pathMatch: 'full'},
    { path: '', redirectTo: '/loans', pathMatch: 'full'},
    { path: 'categories', component: CategoryListComponent },
    { path: 'authors', component: AuthorListComponent },
    { path: 'games', component: GameListComponent },
    { path : 'customers', component: CustomerListComponent },
    { path : 'loans', component: LoanListComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }