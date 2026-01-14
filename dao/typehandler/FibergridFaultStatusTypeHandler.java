package gr.deddie.pfr.dao.typehandler;

import gr.deddie.pfr.model.FibergridFaultStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis type handler for FibergridFaultStatus enum.
 * Converts between database string values and Java enum.
 */
@MappedTypes(FibergridFaultStatus.class)
public class FibergridFaultStatusTypeHandler extends BaseTypeHandler<FibergridFaultStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, FibergridFaultStatus parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public FibergridFaultStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : FibergridFaultStatus.fromValue(value);
    }

    @Override
    public FibergridFaultStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : FibergridFaultStatus.fromValue(value);
    }

    @Override
    public FibergridFaultStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : FibergridFaultStatus.fromValue(value);
    }
}
