import { Customer } from 'src/app/customer/model/Customer';
import { Game } from 'src/app/game/model/Game';

export class Loan {
  id: number;
  game: Game;
  customer: Customer;
  date_loan: Date;
  date_return: Date | null;
}
