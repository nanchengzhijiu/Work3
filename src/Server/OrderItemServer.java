package Server;

import Util.MybatisUtils;
import entity.OrderItem;
import mapper.OrderItemMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class OrderItemServer {
    private SqlSession session= MybatisUtils.getSqlSession(true);
    private OrderItemMapper OrderItemMapper = session.getMapper(OrderItemMapper.class);
    public void insertOrderItem(OrderItem orderItem){
        OrderItemMapper.insertOrderItem(orderItem);
    }
    public boolean deleteOrderItemByNumber(String orderNumber,String commodityNumber){
        int result=OrderItemMapper.deleteOrderItemByNumber(orderNumber,commodityNumber);
        return result > 0;
    }
    public boolean updateOrderItem(OrderItem orderItem){
        int result=OrderItemMapper.updateOrderItem(orderItem);
        return result > 0;
    }
    public OrderItem getOrderItemByNumber(String orderNumber,String commodityNumber){
        return OrderItemMapper.selectItemByNumber(orderNumber,commodityNumber);
    }
}
