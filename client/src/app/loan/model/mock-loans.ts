import { Category } from 'src/app/category/model/Category';
import { Loan } from './Loan';
import { Author } from 'src/app/author/model/Author';
import { LoanPage } from './LoanPage';

export const LOAN_DATA: LoanPage = {
    content: [
        {
          id: 1,
          game: {
              id: 1,
              title: 'game 1',
              age: 0,
              category: new Category,
              author: new Author
          },
          customer: { id: 1, name: 'customer 1' },
          date_loan: new Date('2020,01,01'),
          date_return: new Date('2020-01-07'),
        },
        {
          id: 2,
          game: {
              id: 2, title: 'game 2',
              age: 0,
              category: new Category,
              author: new Author
          },
          customer: { id: 2, name: 'customer 2' },
          date_loan: new Date('2020-01-01'),
          date_return: new Date('2020-01-07'),
        },
        {
          id: 3,
          game: {
              id: 3, title: 'game 3',
              age: 0,
              category: new Category,
              author: new Author
          },
          customer: { id: 3, name: 'customer 3' },
          date_loan: new Date('2020-01-01'),
          date_return: new Date('2020-01-07'),
        },
        {
          id: 4,
          game: {
              id: 4, title: 'game 4',
              age: 0,
              category: new Category,
              author: new Author
          },
          customer: { id: 4, name: 'customer 4' },
          date_loan: new Date('2020-01-01'),
          date_return: new Date('2020-01-07'),
        },
      ], 

       pageable : {
        pageSize: 5,
        pageNumber: 0,
        sort: [
            {property: "id", direction: "ASC"}
        ]
    },
    totalElements: 7
}