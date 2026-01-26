package Server;
import pojo.Order;
import mapper.OrderMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class OrderServer {
    public boolean insertOrder(Order order,SqlSession session){
        OrderMapper orderMapper=session.getMapper(OrderMapper.class);
        try {
            int result=orderMapper.insertOrder(order);
            return result > 0;
        }catch (Exception e){
            if (e.getMessage().contains("order_number")){
                System.out.println("订单编号必须唯一");
            }
        }
        return false;
    }
    public boolean deleteOrder(String orderNumber,SqlSession session){
        OrderMapper orderMapper=session.getMapper(OrderMapper.class);
        int result=orderMapper.deleteOrderByOrderNumber(orderNumber);
        return result > 0;
    }
    public void updateOrder(Order order,SqlSession session){
        OrderMapper orderMapper=session.getMapper(OrderMapper.class);
        orderMapper.updateOrder(order);
        System.out.println("成功更新订单");
    }
    public List<Order> getOrderByPage(int page,SqlSession session){
        OrderMapper orderMapper=session.getMapper(OrderMapper.class);
        List<Order> orders=orderMapper.selectOrderByPage(5,(page-1)*5);
        if (!orders.isEmpty()){
            System.out.println("成功查询到商品,共"+orders.size()+"个商品");
        }else {
            System.out.println("超出页码范围");
        }
        return orders;
    }
    public Order getOrderByNumber(String orderNumber,SqlSession session){
        OrderMapper orderMapper=session.getMapper(OrderMapper.class);
        return orderMapper.selectByOrderNumber(orderNumber);
    }
    public List<Order> getOrderDetail(String orderNumber,SqlSession session){
        OrderMapper orderMapper=session.getMapper(OrderMapper.class);
        return orderMapper.selectOrderDetail(orderNumber);
    }
}
