package gr.deddie.pfr.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by M.Masikos on 16/5/2017.
 */
public interface THALISMapper {

    //--------------check whether supply is disconnected for debts----------------------------------
    @Select("select [dbo].[udf_CustomerCode_DisconnectedCustomerForDebts](#{no}, #{lastTenant})")
    public Boolean isSupplyDisconnectedForDebts(@Param("no") String no, @Param("lastTenant") String lastTenant);

}
