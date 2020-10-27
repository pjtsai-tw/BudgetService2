import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetService {
    Map<String, Budget> bmap = new HashMap<>();

    public BudgetService(IBudgetRepo repo) {
        List<Budget> budgets = repo.getAll();
        for (Budget b : budgets) {
            bmap.put(b.yearMonth, b);
        }
        
    }

    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0;
        }

        List<DatePeriod> dps = parseInput(start, end);
        double result = 0.0;
        for (DatePeriod dp: dps) {
            int days = Period.between(dp.start, dp.end).getDays()+1;
            result += days*getBudgetPerDay(dp);
        }
        return result;
    }

    private double getBudgetPerDay(DatePeriod dp) {
        Budget b =  bmap.get(String.format("%04d",dp.start.getYear()) + String.format("%02d", dp.start.getMonthValue()));
        int amount = (b == null) ? 0 : b.amount;
        return (double)amount / (double)YearMonth.of(dp.start.getYear(), dp.start.getMonth()).lengthOfMonth();
    }

    public List<DatePeriod> parseInput(LocalDate startInput, LocalDate endInput) {
        LocalDate start = startInput;
        LocalDate lastDayOfMonth= start.with(TemporalAdjusters.lastDayOfMonth());

        List<DatePeriod> periods = new ArrayList<DatePeriod>();
        while (lastDayOfMonth.isBefore(endInput)) {
            DatePeriod dp = new DatePeriod();
            dp.start = start;
            dp.end = lastDayOfMonth;
            periods.add(dp);

            start = lastDayOfMonth.with(TemporalAdjusters.firstDayOfNextMonth());
            lastDayOfMonth = start.with(TemporalAdjusters.lastDayOfMonth());
        }

        DatePeriod dp = new DatePeriod();
        dp.start = start;
        dp.end = endInput;
        periods.add(dp);

        return periods;
    }

}
