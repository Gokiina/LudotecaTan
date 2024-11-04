package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.LoanSearchDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.customer.model.CustomerDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    public static final Long EXISTS_LOAN_ID = 1L;
    public static final Long NOT_EXISTS_LOAN_ID = 0L;

    private static final Long NOT_EXISTS_GAME = 0L;
    private static final Long EXISTS_GAME = 3L;

    private static final Long NOT_EXISTS_CUSTOMER = 0L;
    private static final Long EXISTS_CUSTOMER = 3L;

    /*
    private static final Date NOT_EXISTS_DATE_LOAN = new Date(0, 0, 0);
    private static final Date EXISTS_DATE_LOAN = new Date(2001, 10, 1);
    private static final Date NOT_EXISTS_DATE_RETURN = new Date(0, 0, 0);
    private static final Date EXISTS_DATE_RETURN = new Date(2001, 10, 10);

    private static final Date NEW_DATE_LOAN = new Date(2000, 9, 2);
    private static final Date NEW_DATE_RETURN = new Date(2000, 9, 10);
    */

    private static final String GAME_ID_PARAM = "idGame";
    private static final String CUSTOMER_ID_PARAM = "idCustomer";
    private static final Date DATE_LOAN_PARAM = Date.from(LocalDate.of(2000, 10, 2).atStartOfDay(ZoneId.systemDefault()).toInstant());
    private static final Date DATE_RETURN_PARAM = Date.from(LocalDate.of(2000, 10, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());

    private static final int TOTAL_LOANS = 6;
    private static final int PAGE_SIZE = 5;
    private static final Long MODIFY_GAME_ID = 3L;
    private static final Long DELETE_GAME_ID = 6L;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };

    ParameterizedTypeReference<List<LoanDto>> responseType = new ParameterizedTypeReference<>() {
    };

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_ID_PARAM, "{" + GAME_ID_PARAM + "}").queryParam(CUSTOMER_ID_PARAM, "{" + CUSTOMER_ID_PARAM + "}").encode().toUriString();
    }

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewGameAndCustomer() {

        long newLoanId = TOTAL_LOANS + 1;
        long newLoanSize = TOTAL_LOANS + 1;

        LoanDto dto = new LoanDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_GAME);
        dto.setGameById(gameDto.getId());

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(EXISTS_CUSTOMER);
        dto.setCustomerById(customerDto.getId());

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(new PageableRequest(0, (int) newLoanSize));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newLoanSize, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(newLoanId)).findFirst().orElse(null);
        assertNotNull(loan);
        assertEquals(EXISTS_GAME, loan.getGame());
        assertEquals(EXISTS_CUSTOMER, loan.getCustomer());
    }

    @Test
    public void modifyWithExistIdShouldModifyAuthorAndCustomer() {

        LoanDto dto = new LoanDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(EXISTS_GAME);
        dto.setGameById(gameDto.getId());

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(EXISTS_CUSTOMER);
        dto.setCustomerById(customerDto.getId());

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_GAME_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(MODIFY_GAME_ID)).findFirst().orElse(null);
        assertNotNull(loan);
        assertEquals(EXISTS_GAME, loan.getGame());
        assertEquals(EXISTS_CUSTOMER, loan.getCustomer());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteGame() {

        long newLoansSize = TOTAL_LOANS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_GAME_ID, HttpMethod.DELETE, null, Void.class);

        LoanSearchDto loanSearchDto = new LoanSearchDto();
        loanSearchDto.setPageable(new PageableRequest(0, TOTAL_LOANS));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(loanSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newLoansSize, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deleteLoanId = TOTAL_LOANS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteLoanId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    ParameterizedTypeReference<List<LoanDto>> responseTypeList = new ParameterizedTypeReference<List<LoanDto>>() {
    };

    @Test
    public void findAllShouldReturnAllAuthor() {

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseTypeList);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().size());
    }

    @Test
    public void findWithoutFiltersShouldReturnAllLoansInDB() {

        int LOANS_WITH_FILTER = 6;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(CUSTOMER_ID_PARAM, null);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsGameShouldReturnLoans() {

        int LOANS_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, EXISTS_GAME);
        params.put(CUSTOMER_ID_PARAM, null);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsCustomerShouldReturnLoans() {

        int LOANS_WITH_FILTER = 2;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(CUSTOMER_ID_PARAM, EXISTS_CUSTOMER);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findExistsGameAndCustomerShouldReturnLoans() {

        int LOANS_WITH_FILTER = 1;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, EXISTS_GAME);
        params.put(CUSTOMER_ID_PARAM, EXISTS_CUSTOMER);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsGameShouldReturnEmpty() {

        int LOANS_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, NOT_EXISTS_GAME);
        params.put(CUSTOMER_ID_PARAM, null);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsCustomerShouldReturnEmpty() {

        int LOANS_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(CUSTOMER_ID_PARAM, NOT_EXISTS_CUSTOMER);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void findNotExistsGameOrCustomerShouldReturnEmpty() {

        int LOANS_WITH_FILTER = 0;

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, NOT_EXISTS_GAME);
        params.put(CUSTOMER_ID_PARAM, NOT_EXISTS_CUSTOMER);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());

        params.put(GAME_ID_PARAM, NOT_EXISTS_GAME);
        params.put(CUSTOMER_ID_PARAM, EXISTS_CUSTOMER);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());

        params.put(GAME_ID_PARAM, NOT_EXISTS_GAME);
        params.put(CUSTOMER_ID_PARAM, NOT_EXISTS_CUSTOMER);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, response.getBody().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewLoan() {

        LoanDto dto = new LoanDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);

        dto.setGameById(EXISTS_GAME);
        dto.setCustomerById(EXISTS_CUSTOMER);
        dto.setDate_loan(Date.from(LocalDate.of(2001, 11, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dto.setDate_return(Date.from(LocalDate.of(2001, 11, 10).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, EXISTS_GAME);
        params.put(CUSTOMER_ID_PARAM, null);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(0, response.getBody().size());

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void modifyWithExistIdShouldModifyLoan() {

        LoanDto dto = new LoanDto();
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1L);

        dto.setGameById(EXISTS_GAME);
        dto.setCustomerById(EXISTS_CUSTOMER);
        dto.setDate_loan(new Date(2005, 5, 5));
        dto.setDate_return(new Date(2005, 5, 10));

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, EXISTS_GAME);
        params.put(CUSTOMER_ID_PARAM, null);

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(0, response.getBody().size());

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(EXISTS_LOAN_ID, response.getBody().get(0).getId());
    }

    @Test
    public void modifyWithNotExistIdShouldThrowException() {

        LoanDto dto = new LoanDto();
        dto.setGameById(EXISTS_GAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}