package Server;

import Util.MybatisUtils;
import entity.Commodity;
import mapper.CommodityMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CommodityServer {
    private SqlSession sqlSession= MybatisUtils.getSqlSession(true);
    private CommodityMapper commodityMapper=sqlSession.getMapper(CommodityMapper.class);

    public boolean insertCommodity(Commodity commodity){
        try {
            int result=commodityMapper.insertCommodity(commodity);
            return result > 0;
        }catch (Exception e){
            if (e.getMessage().contains("commodity_number")){
                System.out.println("商品编号必须唯一");
            }
        }
        return false;
    }

    public boolean deleteCommodity(String commodityNumber){
        try {
            int result= commodityMapper.deleteCommodityByNumber(commodityNumber);
            return result > 0;
        }catch (Exception e){
            if (e.getMessage().contains("a foreign key constraint fails")){
                System.out.println("必须先删除关联的订单");
            }
        }
       return false;
    }
    public boolean updateCommodity(Commodity commodity){
        int result=commodityMapper.updateCommodity(commodity);
        return result>0;
    }
    public List<Commodity> getCommodity(){
        List<Commodity> commodities=commodityMapper.selectAllCommodity();
        System.out.println("成功查询到商品,共"+commodities.size()+"个商品");
        return commodities;
    }
    public double getCommodityPriceByNumber(String commodityNumber){
        try {
            return commodityMapper.selectCommodityPriceByNumber(commodityNumber);
        }catch (Exception e){
            if (e.getMessage().contains("null")){
                System.out.println("输入的商品编号不存在");
            }
        }
        return -1.0;
    }
}
