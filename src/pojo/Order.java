package pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Order {
    int id;
    String orderNumber;
    LocalDateTime orderTime;  // 使用 LocalDateTime
    double price;
    OrderItem orderItem;
}