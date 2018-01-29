package iso.time;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimeDuration implements TemporalAmount {
    private Period period = Period.ZERO;
    private Duration duration = Duration.ZERO;

    private TimeDuration() {
        
    }
    public long getYears() {
        return period.getYears();
    }
    public long getMonths() {
        return period.getMonths();
    }
    public long getDays() {
        return period.getDays();
    }
    public long getHours() {
        return duration.toHours();
    }
    public long getMinutes() {
        return duration.toMinutes();
    }
    public long getSeconds() {
        return duration.getSeconds();
    }
    /**
     * Parse a ISO 8601 Duration String into a {@link TimeDuration} object
     * @param duration
     * @return
     */
    public static TimeDuration parse(String duration) {
        TimeDuration d = new TimeDuration();
        String[] parts = duration.split("T");
        if (parts.length == 1) {
            d.period = Period.parse(duration);
            d.duration = Duration.ZERO;
        }
        else {
            if ( parts[0].length()>1) { 
                d.period = Period.parse(parts[0]);
            }
            d.duration = Duration.parse("PT"+parts[1]);
            if ( d.duration.toDays() > 0) {
                long days = d.duration.toDays();
                d.duration = d.duration.minusDays(days);
                d.period = d.period.plusDays(days);
            }
        }
        return d;
    }
    public static TimeDuration of(Period period, Duration duration) {
        TimeDuration d = new TimeDuration();
        d.period = period;
        d.duration = duration;
        if (d.duration.toDays()>0) {
            d.duration = d.duration.minusDays(duration.toDays());
            d.period =d.period.plusDays(duration.toDays());
        }
        return d;
    }
    @Override
    public long get(TemporalUnit unit) {
        if ( period != null && period.getUnits().contains(unit) ) {
            return period.get(unit);
        }
        if ( duration != null && duration.getUnits().contains(unit) ) {
            return duration.get(unit);
        }
        return 0;
    }

    @Override
    public List<TemporalUnit> getUnits() {
        Set<TemporalUnit> n = new HashSet<TemporalUnit>();
        n.addAll(period.getUnits());
        n.addAll(duration.getUnits());
        List<TemporalUnit> r = new ArrayList<TemporalUnit>();
        r.addAll(n);
        return r;
    }

    @Override
    public Temporal addTo(Temporal temporal) {
        temporal = temporal.plus(period);
        temporal = temporal.plus(duration);
        return temporal;
    }

    @Override
    public Temporal subtractFrom(Temporal temporal) {
        temporal = temporal.minus(period);
        temporal = temporal.minus(duration);
        return temporal;
    }
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (period.isZero()) {
            b.append( duration.toString());
        }
        else {
            b.append(period.toString());
            if (! duration.isZero() ) {
                b.append(duration.toString().substring(1));
            }
        }
        return b.toString();
    }

}
