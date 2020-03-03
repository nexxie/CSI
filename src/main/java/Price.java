
import java.util.Date;
import java.util.Objects;


public class Price {
    private long id; // идентификатор в БД
    private String productCode; // код товара
    private int number; // номер цены
    private int depart; // номер отдела
    private Date begin; // начало действия
    private Date end; // конец действия
    private long value; // значение цены в копейках

    public Price(String productCode,int number,int depart, Date begin,Date end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Price) {
            Price p = (Price) obj;
            return productCode.equals(p.getProductCode()) && number == p.getNumber() &&
                    depart == p.getDepart() && begin.compareTo(p.getBegin()) == 0 &&
                    end.compareTo(p.getEnd()) == 0 && value == p.getValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, number, depart, begin, end, value);
    }

    public String getProductCode() {
        return productCode;
    }

    public int getNumber() {
        return number;
    }

    public int getDepart() {
        return depart;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public long getValue() {
        return value;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

}