package aula.tareas.mapper;

import aula.tareas.entity.TareaEntityJdbc;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TareaRowMapperJdbc implements RowMapper<TareaEntityJdbc>{
        @Override
        public TareaEntityJdbc mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TareaEntityJdbc(
                    rs.getLong("id"),
                    rs.getString("titulo")
            );
        }
    }

