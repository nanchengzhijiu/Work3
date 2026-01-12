package mapper;

import entity.Commodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommodityMapper {
    int insertCommodity(Commodity commodity);
    int deleteCommodityByNumber(@Param("commodityNumber") String Number);
    int updateCommodity(Commodity commodity);
    List<Commodity> selectCommodityByPage(@Param("pageNumber") int pageNumber,@Param("offect") int offect);
    double selectCommodityPriceByNumber(@Param("commodityNumber") String Number);
}
