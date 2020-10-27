import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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
        givenBudget();
        givenDay(1);
        budgetShouldBe(100.0);
    }
    
    @Test
    public void test_two_days() {
        givenBudget();
        givenDay(2);
        budgetShouldBe(200.0);
    }

    private void givenDay(int day) {
        start = LocalDate.of(2020, Month.JANUARY, 1);
        end = LocalDate.of(2020, Month.JANUARY, day);
    }

    private void budgetShouldBe(double v) {
        double result = budgetService.query(start, end);
        assertEquals(v, result);
    }

    private void givenBudget() {
        IBudgetRepo iBudgetRepo = mock(IBudgetRepo.class);
        Budget budget = new Budget("200001", 3100);
        List<Budget> list = new ArrayList<Budget>();
        list.add(budget);
        when(iBudgetRepo.getAll()).thenReturn(list);
        budgetService = new BudgetService(iBudgetRepo);
    }

    @Test
    public void startAfterEnd() {
        assertTrue(new BudgetService(null).query(LocalDate.of(2020, Month.JANUARY, 2), LocalDate.of(2020, Month.JANUARY, 1)) == 0.0);
    }

}
