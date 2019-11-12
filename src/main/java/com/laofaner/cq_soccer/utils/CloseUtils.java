package com.laofaner.cq_soccer.utils;

/**
 *
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

/**
 * 关闭工具类 <br>
 * 关闭所有带close方法的对象
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class CloseUtils
{
    /**
     * 日志记录
     */
    private static final Log logger = LogFactory.getLog(CloseUtils.class);

    /**
     * 关闭结果集
     *
     * @param rs
     *            结果集
     * @see [类、类#方法、类#成员]
     */
    public static void close(ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            } catch (SQLException e)
            {
                logger.error("close ResultSet failed.", e);
            }
        }
    }

    /**
     * 关闭执行列表
     *
     * @param stmt
     *            执行列表
     * @see [类、类#方法、类#成员]
     */
    public static void close(Statement stmt)
    {
        if (stmt != null)
        {
            try
            {
                stmt.close();
            } catch (SQLException e)
            {
                logger.error("close Statement failed.", e);
            }
        }
    }

    /**
     * 关闭PreparedStatement
     *
     * @param stmt
     *            预编译的SQL执行列表
     * @see [类、类#方法、类#成员]
     */
    public static void close(PreparedStatement stmt)
    {
        if (stmt != null)
        {
            try
            {
                stmt.close();
            } catch (SQLException e)
            {
                logger.error("close PreparedStatement failed.", e);
            }
        }
    }

    /**
     * 关闭连接
     *
     * @param connection
     *            连接
     * @see [类、类#方法、类#成员]
     */
    public static void close(Connection connection)
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            } catch (SQLException e)
            {
                logger.error("close Connection failed.", e);
            }
        }
    }

    /**
     * 关闭数据域或者目标 <功能详细描述>
     *
     * @param
     *            数据源或者目标
     * @see [类、类#方法、类#成员]
     */
    public static void close(AutoCloseable... closes)
    {
        for (AutoCloseable closeable : closes)
        {
            if (closeable != null)
            {
                try
                {
                    closeable.close();
                } catch (Exception e)
                {
                    logger.error("close closeable failed.", e);
                }
            }
        }
    }

    /**
     * 关闭套接字连接
     *
     * @param psocket
     *            套接字连接
     * @see [类、类#方法、类#成员]
     */
    public static void close(Socket psocket)
    {
        if (psocket != null)
        {
            try
            {
                psocket.close();
            } catch (IOException e)
            {
                logger.error("close socket failed.", e);
            }
        }
    }

    /**
     * 关闭服务器套接字
     *
     * @param psocket
     *            服务器套接字连接
     * @see [类、类#方法、类#成员]
     */
    public static void close(ServerSocket psocket)
    {
        if (psocket != null)
        {
            try
            {
                psocket.close();
            } catch (IOException e)
            {
                logger.error("close socket failed.", e);
            }
        }
    }

    /**
     * 万能关闭方法 <br>
     * 关闭带close方法的对象
     *
     * @param objs
     *            对象
     * @see [类、类#方法、类#成员]
     */
    public static void close(Object... objs)
    {
        for (Object obj : objs)
        {
            if (obj != null)
            {
                try
                {
                    Class<?> objClass = obj.getClass();
                    Method meyhod = objClass.getMethod("close", new Class[0]);

                    if (null != meyhod)
                    {
                        meyhod.invoke(obj, new Object[0]);
                    } else
                    {
                        logger.error("This object can not be close, because this object don not have close method.");
                    }
                } catch (Exception e)
                {
                    logger.error("close Object failed.", e);
                }
            }
        }
    }
}
