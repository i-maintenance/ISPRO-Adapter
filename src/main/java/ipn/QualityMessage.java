package ipn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class QualityMessage extends Message<ProductQuality> {
    private ProductQuality result;
    public QualityMessage() {

    }

    public QualityMessage(ProductQuality q) {
	super(q);
    }

}
