package mapper;

import entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int insertOrder(Order order);
    int updateOrder(Order order);
    int deleteOrderByOrderNumber(@Param("orderNumber") String orderNumber);
    List<Order> selectAllOrder();
    Order selectByOrderNumber(@Param("orderNumber") String orderNumber);
}
