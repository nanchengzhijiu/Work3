package pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderItem {
    int id;
    String orderNumber;
    String commodityNumber;
    int number;
    double totalPrice;
    Order order;
}
