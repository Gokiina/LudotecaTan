import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { LoanService } from '../loan.service';
import { PageEvent } from '@angular/material/paginator';
import { Pageable } from 'src/app/core/model/page/Pageable';
import { Loan } from '../model/Loan';
import { LoanEditComponent } from '../loan-edit/loan-edit.component';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';
import { Game } from 'src/app/game/model/Game';
import { Customer } from 'src/app/customer/model/Customer';
import { GameService } from 'src/app/game/game.service';
import { CustomerService } from 'src/app/customer/customer.service';
import { LoanPage } from '../model/LoanPage';

@Component({
  selector: 'app-loan-list',
  templateUrl: './loan-list.component.html',
  styleUrls: ['./loan-list.component.scss'],
})
export class LoanListComponent implements OnInit {
  games: Game[];
  customers: Customer[];

  filterGame: Game;
  filterCustomer: Customer;
  filterDate: Date;

  loans: Loan[];
  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;
  loansPage: LoanPage | null = null;
  dataSource = new MatTableDataSource<Loan>();
  displayedColumns: string[] = [
    'id',
    'game',
    'customer',
    'date_loan',
    'date_return',
    'action',
  ];
  selectedGameId: any;

  constructor(
    private cdr: ChangeDetectorRef,
    private loanService: LoanService,
    public dialog: MatDialog,
    private gameService: GameService,
    private customerService: CustomerService
  ) {}

  ngOnInit(): void {
    this.filterGame = null;
    this.filterCustomer = null;
    this.filterDate = null;

    this.loadPage();
    this.gameService.getGames().subscribe((games) => (this.games = games));
    this.customerService
      .getCustomers()
      .subscribe((customers) => (this.customers = customers));
  }

      
  loadPage(event?: PageEvent) {
    let pageable: Pageable = {
      pageNumber: this.pageNumber,
      pageSize: this.pageSize,
      sort: [
        {
          property: 'id',
          direction: 'ASC',
        },
      ],
    };

    if (event != null) {
      pageable.pageSize = event.pageSize;
      pageable.pageNumber = event.pageIndex;
    }

    const filters = {
      game: this.filterGame ? this.filterGame.id : undefined,
      customer: this.filterCustomer ? this.filterCustomer.id : undefined,
      date: this.filterDate ? this.filterDate : undefined,
    };

    this.loanService.getLoans(filters, pageable).subscribe(
      (data) => {
        this.dataSource.data = data.content;
        this.pageNumber = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalElements = data.totalElements;
        console.log('Filtered Loans Data: (loadpage)', data);
      },
      (error) => {
        console.error('Error loading loans', error);
      }
    );
  }

  onSearch(event?: PageEvent): void {
    let pageable: Pageable = {
      pageNumber: this.pageNumber,
      pageSize: this.pageSize,
      sort: [
        {
          property: 'id',
          direction: 'ASC',
        },
      ],
    };

    if (event != null) {
      pageable.pageSize = event.pageSize;
      pageable.pageNumber = event.pageIndex;
    }

    const filters = {
      game: this.filterGame ? this.filterGame.id : undefined,
      customer: this.filterCustomer ? this.filterCustomer.id : undefined,
      date: this.filterDate,
    };

    this.loanService.getLoans(filters, pageable).subscribe(
      (data) => {
        this.loans = data.content;
        this.totalElements = data.totalElements;
        this.dataSource.data = this.loans;
        console.log('Filtered Loans Data (onsearch):', data);
      },
      (error) => {
        console.error('Error loading loans', error);
      }
    );
  }

  onCleanFilter(): void {
    this.filterGame = null;
    this.filterCustomer = null;
    this.filterDate = null;
    this.loadPage();
  }

  createLoan() {
    const dialogRef = this.dialog.open(LoanEditComponent, {
      data: {},
    });
    dialogRef.afterClosed().subscribe((result) => {
      this.ngOnInit();
    });
  }

  deleteLoan(loan: Loan) {
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      data: {
        title: 'Eliminar reserva',
        description:
          'Atención si borra la reserva se perderán sus datos.<br> ¿Desea eliminar la reserva?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        console.log("loan and Loan " + loan, Loan);
        this.loanService.deleteLoan(loan.id).subscribe(() => {
          this.ngOnInit();
        });
      }
    });
  }
}
