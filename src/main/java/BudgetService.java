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

        final List<DatePeriod> dps = getDatePeriodList(start, end);
        double result = 0.0;
        for (DatePeriod dp : dps) {
            YearMonth yearMonth = YearMonth.from(dp.start);
            final int days = Period.between(dp.start, dp.end).getDays() + 1;
            result += getBudget(yearMonth) * days / yearMonth.lengthOfMonth();
        }
        return result;
    }

    private double getBudget(YearMonth yearMonth) {
        final Budget b = bmap.get(String.format("%04d%02d", yearMonth.getYear(), yearMonth.getMonthValue()));
        return (b == null) ? 0 : b.amount;
    }

    private List<DatePeriod> getDatePeriodList(LocalDate startInput, LocalDate endInput) {

        final List<DatePeriod> periods = new ArrayList<DatePeriod>();
        LocalDate start;
        for (start = startInput; lastDayOfMonth(start).isBefore(endInput); start = firstDayOfNextMonth(start)) {
            periods.add(new DatePeriod(start, lastDayOfMonth(start)));
        }

        periods.add(new DatePeriod(start, endInput));

        return periods;
    }

    private LocalDate firstDayOfNextMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfNextMonth());
    }

    private LocalDate lastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

}
