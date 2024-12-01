package in.ac.iitj.instiapp.database.entities.CustomTypes;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import java.util.Arrays;

import java.io.Serializable;
import java.sql.*;

public class LongArray implements UserType<Long[]> {


    @Override
    public int getSqlType() {
        return Types.ARRAY;
    }

    @Override
    public Class<Long[]> returnedClass() {
        return Long[].class;
    }

    @Override
    public boolean equals(Long[] longs, Long[] j1) {
      if((longs != null && j1 == null) || (longs == null && j1 != null)) {
          return  false;
      }
      if(longs == null) {
          return  true;
      }
      return Arrays.equals(longs, j1);

    }

    @Override
    public int hashCode(Long[] longs) {
        if (longs == null) {
            return 0; // Return 0 for null arrays
        }
        return Arrays.hashCode(longs);
    }

    @Override
    public Long[] nullSafeGet(ResultSet resultSet, int i, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws SQLException {
        Array array = resultSet.getArray(i);
        return  array != null ? (Long[]) array.getArray() : null;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Long[] longs, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws SQLException {
        if(preparedStatement != null){
            if(longs != null){
                try (Connection connection = sharedSessionContractImplementor.getJdbcConnectionAccess().obtainConnection()) {
                    Array array = connection.createArrayOf("bigint", longs);
                    preparedStatement.setArray(i, array);
                } catch (SQLException e) {
                    throw new RuntimeException("Error while setting array parameter", e);
                }
             }
        }
    }

    @Override
    public Long[] deepCopy(Long[] longs) {
        if (longs == null) {
            return null; // Return null if the input array is null
        }
        return longs.clone();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Long[] longs) {
        if (longs == null) {
            return null; // Return null if the input array is null
        }
        return longs.clone();
    }

    @Override
    public Long[] assemble(Serializable serializable, Object o) {
        if (serializable == null) {
            return null;
        }
        return (Long[]) serializable;
    }
}