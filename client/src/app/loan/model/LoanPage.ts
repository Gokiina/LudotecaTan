import { Pageable } from 'src/app/core/model/page/Pageable';
import { Loan } from './Loan';

export class LoanPage {
  content: Loan[];
  totalElements: number;
  totalPages: number;
  pageable: Pageable;
}
