package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.LoanSearchDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collectors;

@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ModelMapper mapper;

    @Operation(summary = "Find Page", description = "Method that returns a page of Loans")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> findPage(@RequestParam(value = "idGame", required = false) Long gameId, @RequestParam(value = "idCustomer", required = false) Long customerId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,  // Asegúrate de que la fecha esté en formato ISO
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize, @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,
            @RequestParam(value = "sortField", defaultValue = "id") String sortField) {
        LoanSearchDto dto = new LoanSearchDto();
        dto.setPageable(new PageableRequest(pageNumber, pageSize));
        if (customerId != null) {
            dto.setCustomer(customerId);
        }
        if (gameId != null) {
            dto.setCustomer(gameId);
        }
        if (date != null) {
            System.out.println("LoanController - data: " + date.toString());
            dto.setDate_loan(date);
        }
        Page<Loan> page = this.loanService.findPage(dto);
        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    @Operation(summary = "Save or Update", description = "Method that saves or updates a Loan")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public ResponseEntity<Void> save(@PathVariable(name = "id", required = false) Long id, @RequestBody LoanDto dto) {
        loanService.save(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws Exception {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}

