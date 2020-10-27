import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BudgetServiceTest {

    private BudgetService budgetService;
    private LocalDate start;
    private LocalDate end;

    @Test
    public void test_one_day() {
        givenBudget(new Budget("200001", 3100));
        givenStartDate(2000, Month.JANUARY, 1);
        givenEndDate(2000, Month.JANUARY, 1);
        budgetShouldBe(100.0);
    }

    @Test
    public void test_two_days() {
        givenBudget(new Budget("200001", 3100));
        givenStartDate(2000, Month.JANUARY, 1);
        givenEndDate(2000, Month.JANUARY, 2);
        budgetShouldBe(200.0);
    }

    @Test
    public void test_one_month() {
        givenBudget(new Budget("200001", 100));
        givenStartDate(2000, Month.JANUARY, 1);
        givenEndDate(2000, Month.JANUARY, 31);
        budgetShouldBe(100.0);
    }

    @Test
    public void test_cross_month() {
        givenBudget(new Budget("199901", 3100), new Budget("199902", 280));
        givenStartDate(1999, Month.JANUARY, 1);
        givenEndDate(1999, Month.FEBRUARY, 1);
        budgetShouldBe(3110.0);
    }

    @Test
    public void test_no_budget() {
        givenBudget(new Budget("200001", 0));
        givenStartDate(2000, Month.JANUARY, 1);
        givenEndDate(2000, Month.JANUARY, 31);
        budgetShouldBe(0.0);
    }

    @Test
    public void test_null_budget() {
        givenBudget(new Budget("200001", 3100),
                new Budget("200003", 310));
        givenStartDate(2000, Month.JANUARY, 31);
        givenEndDate(2000, Month.MARCH, 1);
        budgetShouldBe(110.0);
    }

    private void givenEndDate(int year, Month month, int dayOfMonth) {
        end = LocalDate.of(year, month, dayOfMonth);
    }

    private void givenStartDate(int year, Month month, int dayOfMonth) {
        start = LocalDate.of(year, month, dayOfMonth);
    }

    private void budgetShouldBe(double v) {
        double result = budgetService.query(start, end);
        assertEquals(v, result);
    }

    private void givenBudget(Budget... budgets) {
        IBudgetRepo iBudgetRepo = mock(IBudgetRepo.class);
        when(iBudgetRepo.getAll()).thenReturn(Arrays.asList(budgets));
        budgetService = new BudgetService(iBudgetRepo);
    }

    @Test
    public void startAfterEnd() {
        givenBudget(new Budget("199901", 3100), new Budget("199902", 280));
        givenStartDate(2020, Month.JANUARY, 2);
        givenEndDate(2020, Month.JANUARY, 1);
        budgetShouldBe(0.0);
    }

}
