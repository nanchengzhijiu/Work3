package Server;

import pojo.OrderItem;
import mapper.OrderItemMapper;
import org.apache.ibatis.session.SqlSession;

public class OrderItemServer {
    public void insertOrderItem(OrderItem orderItem,SqlSession session){
        try{
            OrderItemMapper OrderItemMapper = session.getMapper(OrderItemMapper.class);
            OrderItemMapper.insertOrderItem(orderItem);
        }catch (Exception e){
            if (e.getMessage().contains("Duplicate entry")){
                System.out.println("输入商品不能重复，请重新创建");
            }
            session.rollback();
        }

    }
    public boolean deleteOrderItemByNumber(String orderNumber,String commodityNumber,SqlSession session){
        OrderItemMapper OrderItemMapper = session.getMapper(OrderItemMapper.class);
        int result=OrderItemMapper.deleteOrderItemByNumber(orderNumber,commodityNumber);
        return result > 0;
    }
    public boolean updateOrderItem(OrderItem orderItem,SqlSession session){
        OrderItemMapper OrderItemMapper = session.getMapper(OrderItemMapper.class);
        int result=OrderItemMapper.updateOrderItem(orderItem);
        return result > 0;
    }
    public OrderItem getOrderItemByNumber(String orderNumber,String commodityNumber,SqlSession session){
        OrderItemMapper OrderItemMapper = session.getMapper(OrderItemMapper.class);
        return OrderItemMapper.selectItemByNumber(orderNumber,commodityNumber);
    }
}
