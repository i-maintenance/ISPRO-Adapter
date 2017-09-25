package ipn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class AlertMessage extends Message<SimpleAlert> {
	
	public AlertMessage() {
		
	}
	public AlertMessage(SimpleAlert sa) {
		super(sa);
	}
	
}
