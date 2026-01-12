package entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Commodity {
    int id;
    String commodityNumber;
    String name;
    double price;
}
