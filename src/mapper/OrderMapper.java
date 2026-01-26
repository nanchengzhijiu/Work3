package mapper;

import pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int insertOrder(Order order);
    void updateOrder(Order order);
    int deleteOrderByOrderNumber(@Param("orderNumber") String orderNumber);
    List<Order> selectOrderByPage(@Param("pageNumber") int pageNumber,@Param("offect") int offect);
    Order selectByOrderNumber(@Param("orderNumber") String orderNumber);
    List<Order> selectOrderDetail(String orderNumber);
}
