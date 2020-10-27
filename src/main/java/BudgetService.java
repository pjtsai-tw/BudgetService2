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
        final List<Budget> budgets = repo.getAll();
        for (Budget b : budgets) {
            bmap.put(b.yearMonth, b);
        }

    }

    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0;
        }

        final List<DatePeriod> dps = parseInput(start, end);
        double result = 0.0;
        for (DatePeriod dp : dps) {
            final int days = Period.between(dp.start, dp.end).getDays() + 1;
            result += days * getBudgetPerDay(YearMonth.from(dp.start));
        }
        return result;
    }

    private double getBudgetPerDay(YearMonth yearMonth) {
        final Budget b = bmap.get(String.format("%04d%02d", yearMonth.getYear(), yearMonth.getMonthValue()));
        final int amount = (b == null) ? 0 : b.amount;
        return (double) amount / (double) yearMonth.lengthOfMonth();
    }

    public List<DatePeriod> parseInput(LocalDate startInput, LocalDate endInput) {
        LocalDate start = startInput;
        LocalDate lastDayOfMonth = start.with(TemporalAdjusters.lastDayOfMonth());

        final List<DatePeriod> periods = new ArrayList<DatePeriod>();
        while (lastDayOfMonth.isBefore(endInput)) {
            periods.add(new DatePeriod(start, lastDayOfMonth));

            start = lastDayOfMonth.with(TemporalAdjusters.firstDayOfNextMonth());
            lastDayOfMonth = start.with(TemporalAdjusters.lastDayOfMonth());
        }

        periods.add(new DatePeriod(start, endInput));

        return periods;
    }

}
