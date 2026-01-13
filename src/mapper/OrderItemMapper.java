package mapper;

import pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int insertOrderItem(OrderItem orderItem);
    int deleteOrderItemByNumber(@Param("orderNumber") String orderNumber,@Param("commodityNumber") String commodityNumber);
    int updateOrderItem(OrderItem orderItem);
    List<OrderItem> selectAllItem();
    OrderItem selectItemByNumber(@Param("orderNumber") String orderNumber,@Param("commodityNumber") String commodityNumber);
}
