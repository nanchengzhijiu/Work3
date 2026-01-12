package View;

import java.util.Scanner;

public class MainView {
    private Scanner scanner=new Scanner(System.in);
    private boolean loop=true;
    private String key="";
    private void exit(){
        do {
            System.out.println("你确认要退出吗?y/n");
            key = scanner.next();
        } while (!key.equals("y") && !key.equals("n"));
        if(key.equals("y")){
            loop=false;
        }
    }
    public void mainView(){
        do {
            System.out.println("1.订单操作");
            System.out.println("2.商品操作");
            System.out.println("3.退出系统");
            System.out.println("请输入想要进行的业务操作：");
            key=scanner.next();
            switch(key){
                case "1":
                    OrderView ov=new OrderView();
                    ov.orderView();
                    break;
                case "2":
                    CommodityView cv=new CommodityView();
                    cv.commodityView();
                    break;
                case "3":
                    exit();
                    break;
                default:
                    System.out.println("输入有误，请重新输入");
                    break;
            }
        }while(loop);
    }
}
