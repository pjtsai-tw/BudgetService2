import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class BudgetService {
    public BudgetService(IBudgetRepo repo) {
        
    }

    public double query(LocalDate start, LocalDate end) {
        List<DatePeriod> dps = parseInput(start, end);
        double result = 0.0;
        for (DatePeriod dp: dps) {
            int days = Period.between(dp.start, dp.end).getDays()+1;
            result += days*getBudgetPerDay(dp);
        }
        return result;
    }

    private double getBudgetPerDay(DatePeriod dp) {
        return 0.0;
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
