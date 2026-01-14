package gr.deddie.pfr.dao.typehandler;

import gr.deddie.pfr.model.FibergridFlagRelated;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis type handler for FibergridFlagRelated enum.
 * Converts between database integer values and Java enum.
 */
@MappedTypes(FibergridFlagRelated.class)
public class FibergridFlagRelatedTypeHandler extends BaseTypeHandler<FibergridFlagRelated> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, FibergridFlagRelated parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public FibergridFlagRelated getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : FibergridFlagRelated.fromValue(value);
    }

    @Override
    public FibergridFlagRelated getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return rs.wasNull() ? null : FibergridFlagRelated.fromValue(value);
    }

    @Override
    public FibergridFlagRelated getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return cs.wasNull() ? null : FibergridFlagRelated.fromValue(value);
    }
}
