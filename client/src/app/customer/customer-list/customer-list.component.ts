import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Customer } from '../model/Customer';
import { CustomerService } from '../customer.service';
import { CustomerEditComponent } from '../customer-edit/customer-edit.component';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.scss']
})
export class CustomerListComponent implements OnInit{

  dataSource = new MatTableDataSource<Customer>();
  displayedColumns: string[] = ['id', 'name', 'action'];
  customers: Customer[] = [];
  
  constructor(
    private customerService: CustomerService,
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.customerService.getCustomers().subscribe(
      customers => this.dataSource.data = customers
    )
  }
  loadCustomers(){
    this.customerService.getCustomers().subscribe(customers=>{
      this.dataSource.data = this.customers;
      this.customers = customers;
    })
  }

  createCustomer(){
    const dialogRef = this.dialog.open(CustomerEditComponent, {
      data: {}

    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });
  }
  editCustomer(customer: Customer) {
    const dialogRef = this.dialog.open(CustomerEditComponent, {
      data: { customer: customer },
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
      this.loadCustomers();
    });
  }
  deleteCustomer(customer: Customer) {    
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      data: { title: "Eliminar cliente", description: "Atención si borra el cliente se perderán sus datos.<br> ¿Desea eliminar el cliente?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.customerService.deleteCustomer(customer.id).subscribe(result => {
          this.ngOnInit();
          this.loadCustomers();
        }); 
      }
    });
  }  
}
