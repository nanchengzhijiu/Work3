package entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class Order {
    int id;
    String orderNumber;
    LocalDateTime orderTime;  // 使用 LocalDateTime
    double price;
    List<OrderItem> orderItems;
}