import { Injectable } from '@angular/core';
import { Pageable } from '../core/model/page/Pageable';
import { map, Observable, of, switchMap, throwError } from 'rxjs';
import { LoanPage } from './model/LoanPage';
//import { LOAN_DATA } from './model/mock-loans';
import { Loan } from './model/Loan';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class LoanService {
  private url = 'http://localhost:8080/loan';

  constructor(private http: HttpClient) {}

  getLoans(
    filters: { game?: number; customer?: number; date?: Date },
    pageable: Pageable
  ): Observable<LoanPage> {
    const urlFilter = this.composeFindUrl(filters, pageable);
    console.log('getloansFilter: ' + urlFilter);
    console.log('pageable:', JSON.stringify(pageable));
    console.log(
      'Sort parameters:',
      pageable.sort.map((s) => `${s.property} ${s.direction}`).join(', ')
    );
    return this.http.post<LoanPage>(urlFilter, { pageable: pageable });
  }
  
  private composeFindUrl(
    filters: { game?: number; customer?: number; date?: Date } | null,
    pageable: Pageable
  ): string {
    let params = '';

    console.log('Filters in URL composition: ', filters);
    console.log('filter game ' + filters.game);

    if (filters !== null) {
      if (filters.game !== null && filters.game !== undefined) {
        params += 'idGame=' + filters.game + '&';
      }
      if (filters.customer !== null && filters.customer !== undefined) {
        params += 'idCustomer=' + filters.customer + '&';
      }
      if (filters.date !== null && filters.date !== undefined) {
        const adjustedDate = new Date(filters.date);
        adjustedDate.setDate(adjustedDate.getDate() + 1);
        const isoDate = adjustedDate.toISOString().split('T')[0];
        params += 'date=' + isoDate + '&';
        console.log('LoanService - isoDate:' + isoDate);
      }
    }
    params += `pageNumber=${pageable.pageNumber}&pageSize=${pageable.pageSize}&`;

    if (params === '') return this.url;
    else {
      console.log('parametros: ' + this.url + '?' + params);
      return this.url + '?' + params;
    }
  }

  saveLoan(loan: Loan): Observable<void> {
    if (!loan.id) {
      console.log("no loan.id");
      return this.http.put<void>(this.url, loan);
    }
  }
  
  deleteLoan(idLoan: number): Observable<void> {
    return this.http.delete<void>(this.url + '/' + idLoan);
  }
}

// Mocks
/*
        export class LoanService {

          constructor() { }
      
          getLoans(pageable : Pageable): Observable<LoanPage> {
              return of(LOAN_DATA);
          }
      
          saveLoan(loan: Loan): Observable<void> {
              return of(null);
          }
          deleteLoan(idLoan: number): Observable<void> {
            return of(null);
        }
}*/
