import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BudgetServiceTest {

    private BudgetService budgetService;
    private LocalDate start;
    private LocalDate end;

    @Test
    public void test_one_day() {
        givenBudget(new Budget("200001", 3100));
        givenDay(1);
        budgetShouldBe(100.0);
    }

    @Test
    public void test_two_days() {
        givenBudget(new Budget("200001", 3100));
        givenDay(2);
        budgetShouldBe(200.0);
    }

    @Test
    public void test_cross_month() {
        givenBudget(new Budget("199901", 3100), new Budget("199902", 280));
        start = LocalDate.of(2020, Month.JANUARY, 1);
        end = LocalDate.of(2020, Month.FEBRUARY, 1);
        budgetShouldBe(3110.0);
    }

    private void givenDay(int day) {
        start = LocalDate.of(2020, Month.JANUARY, 1);
        end = LocalDate.of(2020, Month.JANUARY, day);
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
        assertTrue(new BudgetService(new IBudgetRepo() {
            @Override
            public List<Budget> getAll() {
                return new LinkedList();
            }
        }).query(LocalDate.of(2020, Month.JANUARY, 2), LocalDate.of(2020, Month.JANUARY, 1)) == 0.0);
    }

}
