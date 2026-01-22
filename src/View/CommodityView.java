package View;

import Server.CommodityServer;
import Util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import pojo.Commodity;
import lombok.Data;

import java.util.Scanner;
@Data
public class CommodityView {
    private Scanner scanner = new Scanner(System.in);
    private boolean loop=true;
    private String key="";
    private CommodityServer commodityServer=new CommodityServer();
    private String commodityNumber;
    private String commodityName;
    private Double commodityPrice;
    private void clearScan(){
        if (scanner.hasNextLine()) {
            scanner.nextLine(); // 清理可能存在的换行符
        }
    }
    private void insertCommodityView() {
        clearScan();
//        需要事务管理
        SqlSession session= MybatisUtils.getSqlSession(false);
        try {
            System.out.println("请输入商品编号：");
            commodityNumber=scanner.next();
            System.out.println("请输入商品名：");
            commodityName=scanner.next();
            System.out.println("请输入商品定价：");
            commodityPrice=scanner.nextDouble();
        } catch (Exception e) {
            System.out.println("输入数据有误，返回上一级操作");
            return;
        }

        boolean isInsert= commodityServer.insertCommodity(
                new Commodity()
                        .setCommodityNumber(commodityNumber)
                        .setName(commodityName)
                        .setPrice(commodityPrice),
                session
        );
        if(isInsert){
            System.out.println("插入成功");
        }else {
            System.out.println("插入失败");
        }
        session.commit();
        session.close();
    }
    private void getCommodityView(){
        int page;
        SqlSession session= MybatisUtils.getSqlSession(true);
        do {
            clearScan();
            System.out.println("请输入你要查询的页码：");
            try {
                page=scanner.nextInt();
                if (page<=0) {
                    System.out.println("请输入正确的页码");
                    continue;
                }
                break;
            }catch (Exception e){
                System.out.println("输入页码格式错误");
            }
        }while(true);
        commodityServer.getCommodityByPage(page,session).forEach(commodity->{
            System.out.println(
                    "商品编号:"+commodity.getCommodityNumber()+
                            " 商品名："+commodity.getName()+
                            " 商品单价："+commodity.getPrice());
        });
        session.close();
    }
    private void deleteCommodityView(){
        clearScan();
        getCommodityView();
        SqlSession session= MybatisUtils.getSqlSession(true);
        System.out.println("请输入要删除的商品的编号");
        key=scanner.next();
        boolean isDelete=commodityServer.deleteCommodity(key,session);
        if (isDelete) {
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败，商品不存在");
        }
        session.close();
    }
    private void updateCommodityView(){
        clearScan();
        SqlSession session= MybatisUtils.getSqlSession(true);
//        为输入编号则一直循环
        do {
            System.out.println("请输入要更改的商品编号(必填)：");
            commodityNumber = scanner.nextLine();
        } while (commodityNumber.isEmpty());
        System.out.println("请输入商品名,不输入输入则不变：");
        commodityName=scanner.nextLine();
        System.out.println("请输入商品定价,不输入则不变：");
        String priceInput = scanner.nextLine();

        if (priceInput!=null && !priceInput.isEmpty()) {
            try {
                commodityPrice=Double.parseDouble(priceInput);
                if (commodityPrice < 0) {
                    System.out.println("价格不能为负数，将跳过价格更新");
                    commodityPrice = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("价格格式错误，将跳过价格更新");
                commodityPrice = null;
            }
            return;
        }
        Commodity commodity = new Commodity()
                .setCommodityNumber(commodityNumber);
        if (!commodityName.isEmpty()) {
            commodity.setName(commodityName);
        }
        if (commodityPrice != null) {
            commodity.setPrice(commodityPrice);
        }
        boolean isUpdate=commodityServer.updateCommodity(commodity,session);
        if (isUpdate){
            System.out.println("更新成功");
        }else {
            System.out.println("更新失败,商品不存在");
        }
        session.close();
    }
    public void commodityView(){
        do {
            System.out.println("1.增加商品");
            System.out.println("2.删除商品");
            System.out.println("3.更新商品");
            System.out.println("4.查询商品");
            System.out.println("5.返回上一级");
            key=scanner.next();
            switch (key){
                case "1":
                    insertCommodityView();
                    break;
                case "2":
                    deleteCommodityView();
                    break;
                case "3":
                    updateCommodityView();
                    break;
                case "4":
                    getCommodityView();
                    break;
                case "5":
                    loop=false;
                    break;
                default:
                    System.out.println("输入有误，请重新输入:");
                    break;
            }
        }while(loop);
    }
}
