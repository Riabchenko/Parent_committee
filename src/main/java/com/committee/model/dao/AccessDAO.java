package com.committee.model.dao;

/**
 * @author Aliona Riabchenko
 * @version 1.0 Build 21.06.2015
 */

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import com.committee.model.action.Action;
import com.committee.model.data.Access;
import com.committee.model.data.AccessImpl;
import com.committee.model.data.TypeOfAccess;
import com.committee.model.util.ConnectionPool;
import com.committee.security.ScryptPasswordHashingDemo;

public class AccessDAO implements Action, Access {
    private ConnectionPool connectionPool;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    // Log4j logger
    private static final Logger log = Logger.getLogger(AccessDAO.class);

    // T - String
    public String handleRequest(HttpServletRequest request,
                                HttpServletResponse response, ConnectionPool dao)
            throws ServletException, IOException {
        connectionPool = dao;
        // get entered authorization info
        String login = request.getParameter("telephone");
        String password = request.getParameter("password");
        String result = null;
        // if nothing is entered (user has just entered the page)
        if (login == "" || password == "") {
            result = "003";
        } else {

            Access access = createAccess(login);
            if (access != null) {
                if (checkPassword(password, access)) {
                    HttpSession session = request.getSession();
                    // если сессия уже существует по этому пользователю, то вернуть ее
                    if (request.getSession().getAttribute("accesUser") != null || request.getSession().getAttribute("accesAdmin") != null) {
                        if (request.getSession().getAttribute("accesUser") != null) {
                            if (request.getSession().getAttribute("accesUser")
                                    .equals(access.getIdAccount())) {
                                result = "user";
                            } else {
                                session.setAttribute("accesUser", access.getIdAccount());
                                result = "user";
                            }

                            log.info("User " + access.getIdAccount() + " has logged in");
                        } else if (request.getSession().getAttribute("accesAdmin") != null) {
                            if (request.getSession().getAttribute("accesAdmin")
                                    .equals(access.getIdAccount())) {
                                result = "admin";
                            } else {
                                session.setAttribute("accesAdmin", access.getIdAccount());
                                result = "admin";
                            }

                            log.info("Admin " + access.getIdAccount() + " has logged in");
                        }
                    } else {

                        if (access.isAdmin()) {
                            session.setAttribute("accesAdmin", access.getIdAccount());
                            log.info("Admin " + access.getIdAccount() + " has logged in");
                            result = "admin";
                        } else if (!access.isAdmin()) {
                            session.setAttribute("accesUser", access.getIdAccount());
                            log.info("User " + access.getIdAccount() + " has logged in");
                            result = "user";
                        } else {
                            result = "004";
                        }
                    }

                } else {
                    result = "018";//wrong password
                }
            } else {
                result = "004";
            }
        }
        return result;
    }

    // check password
    public boolean checkPassword(String enterPassword, Access access) {
        boolean is = false;
        // -----this checks password-----//
//create

//        ScryptPasswordHashingDemo sph = new ScryptPasswordHashingDemo();
//        return sph.matched(enterPassword, access.getPassword());

        return true;
        // ---------------------------------//
    }

    public static boolean isAdmin(Access access) {
        try {
            if (access.getTypeOfAccess().equals(TypeOfAccess.ADMIN))
                return true;
        } catch (SQLException e) {
            log.error("Error access" + e.getMessage());
        }
        return false;
    }

    public Access createAccess(String login) {
        Access access = null;
        Connection conn = null;

        try {
            conn = connectionPool.retrieve();
            String sql = "SELECT `typeOfAccess`, `Parents_id_parents`, `password` "
                    + "FROM `Access` WHERE `Parents_id_parents` = "
                    + "(SELECT `Parents_id_parents` FROM `Telephones` "
                    + "WHERE `number` = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, login);
            rs = ps.executeQuery(); // выполнение запроса
            rs.last();
            int rowsOfAccess = rs.getRow();
            rs.beforeFirst();
            if (rowsOfAccess == 0) {
                log.error("Access don't search for telephone =" + login);
            } else {
                access = new AccessImpl();
                access.setTelephone(login);
                while (rs.next()) {
                    access.setTypeOfAccess(rs.getString(1));
                    access.setIdAccount(rs.getInt(2));
                    access.setPassword(rs.getString(3));
                }
            }

            sql = null;
            rs.close();
            ps.close();
            conn.commit();
        } catch (SQLException e) {
            log.error("SQLException - Error request at the access!" + e.getMessage());
        } finally {
            connectionPool.putback(conn);
        }

        return access;
    }

    public String setPassword(String password) {
        // TODO Auto-generated method stub
        return null;
    }

    public TypeOfAccess setTypeOfAccess(String access) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    public TypeOfAccess getTypeOfAccess() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public int getIdAccount() {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean isAdmin() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setIdAccount(int idAccount) {
        // TODO Auto-generated method stub

    }

    public String setTelephone(String login) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTelephone(String telephone) {
        // TODO Auto-generated method stub
        return null;
    }

}
