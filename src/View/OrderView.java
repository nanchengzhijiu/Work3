package View;
import Server.CommodityServer;
import Server.OrderItemServer;
import Server.OrderServer;
import entity.Order;
import entity.OrderItem;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class OrderView {
    private Scanner scanner = new Scanner(System.in);
    private boolean loop=true;
    private String key="";
    private OrderServer orderServer=new OrderServer();
    private CommodityServer commodityServer=new CommodityServer();
    private OrderItemServer orderItemServer=new OrderItemServer();
    private String orderNumber;
    private Double orderPrice=0.0;
//    订单商品集合
    private List<OrderItem> orderItemList=new ArrayList<>();
    private void clearScan(){
        if (scanner.hasNextLine()) {
            scanner.nextLine(); // 清理可能存在的换行符
        }
    }
//    输入订单信息
    private void inputOrder(){
        clearScan();
        //订单编号输入
        do {
            System.out.println("请输入订单编号(必填)：");
            orderNumber = scanner.next();
        } while (orderNumber.isEmpty());
    }
//    输入订单项信息
    private void inputOrderItem(){
        clearScan();
        String orderInfo;
        String[] info;
        orderItemList.clear();
        orderPrice=0.0;
        //        输入订单商品
        do {
            System.out.println("请输入订单包含的商品信息,格式为 商品编号 商品数量,输入0结束输入：");
            orderInfo=scanner.nextLine();
            if (orderInfo.equals("0")) {
                break;
            }else {
                info=orderInfo.split(" ");
                if (info.length!=2) {
                    System.out.println("输入格式有误");
                    continue;
                }
//              该商品的总金额
                double totalPrice=commodityServer.getCommodityPriceByNumber(info[0])*Double.parseDouble(info[1]);
                if (totalPrice<0){
                    continue;
                }
                orderItemList.add(new OrderItem()
                        .setCommodityNumber(info[0])
                        .setNumber(Integer.parseInt(info[1]))
                        .setTotalPrice(totalPrice));
//                计算订单总额
                orderPrice+=totalPrice;
            }
        }while(true);
    }
//    插入订单和订单项
    private void insertOrder(){
        //        插入订单数据
        boolean isInsert=orderServer.insertOrder(
                new Order()
                        .setOrderNumber(orderNumber)
                        .setPrice(orderPrice)
        );
        //建立订单条目成功后插入商品条目
        if (isInsert){
            orderItemList.forEach(orderItem->{
                orderItemServer.insertOrderItem(
                        new OrderItem()
                                .setOrderNumber(orderNumber)
                                .setCommodityNumber(orderItem.getCommodityNumber())
                                .setNumber(orderItem.getNumber())
                                .setTotalPrice(orderItem.getTotalPrice())
                );
            });
            System.out.println("创建订单成功");
        }
    }
    private void insertOrderView(){
        inputOrder();
        inputOrderItem();
        insertOrder();
    }
    private void getAllOrderView(){
        orderServer.getOrder().forEach(o->{
            System.out.println(
                    "订单号"+o.getOrderNumber()+
                            " 价格："+o.getPrice()+
                            " 下单时间："+
                            o.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );
        });
    }
    private void deleteOrderView(){
        clearScan();
        System.out.println("请输入要删除的订单编号：");
        orderNumber=scanner.next();
        boolean isDelete=orderServer.deleteOrder(orderNumber);
        if (isDelete){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败,无对应记录");
        }
    }
    private void deleteOrderItemView(){
        clearScan();
        System.out.println("请输入需要删除的商品编号：");
        String commodityNumber=scanner.next();
        OrderItem orderItem=orderItemServer.getOrderItemByNumber(commodityNumber,orderNumber);
//        若无对应记录，则重新输入
        if (orderItem==null){
            System.out.println("无对应商品记录,请重新输入");
            return;
        }
//        获取订单总价
        orderPrice=orderServer.getOrderByNumber(orderNumber).getPrice();
        orderPrice-=orderItem.getTotalPrice();
//        更新订单
        orderServer.updateOrder(new Order().setOrderNumber(orderNumber).setPrice(orderPrice));
        boolean isDelete=orderItemServer.deleteOrderItemByNumber(commodityNumber,orderNumber);
        if (isDelete){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }

    }
    private void updateOrderItemView(){
        clearScan();
        System.out.println("请输入想要更新的商品编号：");
        String commodityNumber=scanner.next();
        System.out.println("请输入想要更新的数量");
        String number=scanner.next();
//        通过商品和订单编号获取唯一的商品项目
        OrderItem orderItem=orderItemServer.getOrderItemByNumber(orderNumber,commodityNumber);
//        商品项不存在
        if(orderItem==null){
            System.out.println("商品项不存在");
            return;
        }
        //        获取当前商品对应订单信息
        Order order=orderServer.getOrderByNumber(orderNumber);
        //        修改订单总价
        double totalPrice=order.getPrice()-orderItem.getTotalPrice();
//        商品单价
        double unitPrice=orderItem.getTotalPrice()/orderItem.getNumber();
//        更新数量
        orderItem.setNumber(Integer.parseInt(number));
//        设置当前商品项总价
        orderItem.setTotalPrice(unitPrice*orderItem.getNumber());
//        设置当前订单总价
        totalPrice+=unitPrice*orderItem.getNumber();
        order.setPrice(totalPrice);
//        更新订单总价
        orderServer.updateOrder(order);
        boolean isUpdate=orderItemServer.updateOrderItem(orderItem);
        if (isUpdate){
            System.out.println("更新成功");
        }else {
            System.out.println("更新失败，找不到商品条项");
        }
    }
    private void updateOrderView(){
        clearScan();
        boolean loop=true;
        System.out.println("请输入需要更改的订单编号：");
        orderNumber=scanner.next();
        if(orderServer.getOrderByNumber(orderNumber)==null){
            System.out.println("无对应订单编号,请重新操作");
            return;
        }
        do {
            System.out.println("请输入对订单列表的操作类型：");
            System.out.println("1.删除相应订单商品项");
            System.out.println("2.更新相应订单商品数量");
            System.out.println("3.退出更新操作");
            key=scanner.next();
            switch (key){
                case "1":
                    deleteOrderItemView();
                    break;
                case "2":
                    updateOrderItemView();
                    break;
                case "3":
                    loop=false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入");
                    break;
            }
        }while (loop);

    }
    public void orderView(){
        do {
            System.out.println("1.增加订单");
            System.out.println("2.删除订单");
            System.out.println("3.更新订单");
            System.out.println("4.查询订单");
            System.out.println("5.返回上一级");
            key=scanner.next();
            switch (key){
                case "1":
                    insertOrderView();
                    break;
                case "2":
                    deleteOrderView();
                    break;
                case "3":
                    updateOrderView();
                    break;
                case "4":
                    getAllOrderView();
                    break;
                case "5":
                    loop=false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入");
                    break;
            }
        }while(loop);
    }
}
