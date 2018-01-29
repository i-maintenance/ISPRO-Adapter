package iso.time;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
/**
 * ISO 8601 Time Interval
 * @author dglachs
 *
 */
public class TimeInterval {
    Temporal when;
    TemporalAmount duration;
    Temporal until;
    
    public TimeInterval(Temporal when) {
        this.when = when;
        this.until = null;
        this.duration = null;
    }
    public TimeInterval(Temporal start, Temporal until) {
        this.when = start;
        
        this.until = until;
        this.duration = null; 
    }
    public TimeInterval(TemporalAmount temporalAmount) {
        this.duration = temporalAmount;
    }
    public TimeInterval(Temporal start, TemporalAmount amount) {
        this.when = start;
        this.duration = amount;
        this.until = null;
    }
    public TimeInterval(TemporalAmount amount, Temporal end) {
        this.when = null;
        this.duration = amount;
        this.until = end;
    }
    public static TimeInterval parse(String fromString) throws DateTimeParseException {
        String[] parts = fromString.split("/");
        if ( parts.length == 1 ) {
            if ( fromString.startsWith("P")) { // Temporal Amount
                return new TimeInterval(parseTemporalAmount(fromString));
            }
            else  { // Time Object
                return new TimeInterval(parseTemporal(fromString));
            }
        }
        else {
            Temporal when = null;
            TemporalAmount amount = null;
            Temporal until = null;
            if ( parts[0].startsWith("P") ) {
                amount = parseTemporalAmount(parts[0]);
            }
            else {
                when = parseTemporal(parts[0]);
            }
            
            if (parts[1].startsWith("P")) {
                amount = parseTemporalAmount(parts[1]);
            }
            else {
                until = parseTemporal(parts[1]);
            }
            if ( amount == null) {
                return new TimeInterval(when, until);
            }
            else {
                if ( until == null) {
                    return new TimeInterval(when, amount);
                }
                else {
                    return new TimeInterval(amount, until);
                }
            }
        }
    }
    public String toString() {
        StringBuilder b = new StringBuilder();
        if ( when != null ) {
            b.append(when.toString());
            if ( duration!= null) {
                b.append("/" + duration.toString());
            }
            else if ( until !=null) {
                b.append("/"+until.toString());
            }
        }
        else {
            if ( duration != null) {
                b.append(duration.toString());
                if ( until != null) {
                    b.append("/"+until.toString());
                }
            }
        }
        return b.toString();
    }
    static Temporal parseTemporal(String part) throws DateTimeParseException {
        if ( part.endsWith("Z") ) {     // instant
            return Instant.parse(part);
        }
        if ( part.contains("+") || part.contains("-")) {
            return ZonedDateTime.parse(part);
        }
        else {
            return LocalDateTime.parse(part);
        }
    }
    static TemporalAmount parseTemporalAmount(String fromString) throws DateTimeParseException {
        if ( fromString.startsWith("PT")) {
            return Duration.parse(fromString);
        }
        else if ( fromString.contains("T")) {
            // Period with Time
            return TimeDuration.parse(fromString);
        }
        else {
            return Period.parse(fromString);
            
        }

    }
}
