import { Component, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { Loan } from '../model/Loan';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LoanService } from '../loan.service';
import { Customer } from 'src/app/customer/model/Customer';
import { CustomerService } from 'src/app/customer/customer.service';
import { GameService } from 'src/app/game/game.service';
import { Game } from 'src/app/game/model/Game';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
} from '@angular/material/core';
import {
  MAT_MOMENT_DATE_FORMATS,
  MomentDateAdapter,
} from '@angular/material-moment-adapter';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-loan-edit',
  templateUrl: './loan-edit.component.html',
  styleUrls: ['./loan-edit.component.scss'],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'es-ES' },
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE],
    },
    { provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS },
  ],
})
export class LoanEditComponent implements OnInit {
  loan: Loan;
  customers: Customer[];
  games: Game[];

  startDate = new FormControl(new Date());
  maxDate = new Date();
  date = { date_loan: new Date(), date_return: null };

  constructor(
    public dialogRef: MatDialogRef<LoanEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private loanService: LoanService,
    private customerService: CustomerService,
    private gameService: GameService
  ) {}

  ngOnInit() {
    if (this.data.loan != null) {
      this.loan = Object.assign({}, this.data.loan);
      this.date.date_loan = new Date(this.loan.date_loan);
      this.date.date_return = this.loan.date_return
        ? new Date(this.loan.date_return)
        : null;
    } else {
      this.loan = new Loan();
    }

    this.updateMaxDate();
    this.startDate.valueChanges.subscribe((newDate) => {
      this.date.date_loan = new Date(newDate);
      this.updateMaxDate();
    });

    this.customerService.getCustomers().subscribe((customers) => {
      this.customers = customers;
      if (this.loan.customer != null) {
        let customerFilter: Customer[] = customers.filter(
          (customer) => customer.id == this.data.loan.customer.id
        );
        if (customerFilter != null) {
          this.loan.customer = customerFilter[0];
        }
      }
    });
    this.gameService.getGames().subscribe((games) => {
      this.games = games;
      if (this.loan.game != null) {
        let gameFilter: Game[] = games.filter(
          (game) => game.id == this.data.loan.game.id
        );
        if (gameFilter != null) {
          this.loan.game = gameFilter[0];
        }
      }
    });
  }

  updateMaxDate() {
    this.maxDate = new Date(this.startDate.value);
    this.maxDate.setDate(this.maxDate.getDate() + 14);
  }

  onSave() {
    this.loan.date_loan = this.date.date_loan;
    this.loan.date_return = this.date.date_return;

    this.loanService.saveLoan(this.loan).subscribe((result) => {
      this.dialogRef.close();
      console.log(this.loan);
    });
  }

  onClose() {
    this.dialogRef.close();
  }
}
