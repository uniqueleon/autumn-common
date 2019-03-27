package org.aztec.autumn.common.utils.jdbc;

import java.sql.SQLException;

@FunctionalInterface
public abstract interface DAOQueryExecutor {

  public abstract Object query() throws SQLException;

}
