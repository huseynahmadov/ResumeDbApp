package com.company.dao.impl;

import com.company.dao.inter.AbstractDAO;
import com.company.dao.inter.UserDaoInter;
import com.company.entity.Country;
import com.company.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends AbstractDAO implements UserDaoInter {

    private User getUser(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String profileDesc = rs.getString("profileDescription");
        int nationalityId = rs.getInt("nationality_id");
        int birthplaceId = rs.getInt("birthplace_id");
        String nationalityStr = rs.getString("nationality");
        String birthplaceStr = rs.getString("birthplace");
        Date birthdate = rs.getDate("birthdate");

        Country nationality = new Country(nationalityId, null, nationalityStr);
        Country birthplace = new Country(birthplaceId, birthplaceStr, null);
        return (new User(id, name, surname, phone, email, profileDesc, birthdate, nationality, birthplace));
    }


    @Override
    public List<User> getAll(String name, String surname, Integer nationalityId) {
        List<User> result = new ArrayList<>();
        try (Connection c = connect()) {


            String sql = "SELECT\n" +
                    "\tu.*,\n" +
                    "\tn.nationality,\n" +
                    "\tc.NAME AS birthplace \n" +
                    "FROM\n" +
                    "\tUSER u\n" +
                    "\tLEFT JOIN country n ON u.nationality_id = n.id\n" +
                    "\tLEFT JOIN country c ON u.birthplace_id = c.id \n" +
                    "WHERE\n" +
                    "\t1 =1";


            if (name != null && !name.trim().isEmpty()) {
                sql += " and u.name=? ";
            }

            if (surname != null && !surname.trim().isEmpty()) {
                sql += " and u.surname=? ";
            }

            if (nationalityId != null) {
                sql += " and u.nationality_id=? ";
            }

            PreparedStatement stmt = c.prepareStatement(sql);

            int i = 1;
            if (name!=null && !name.trim().isEmpty()){
                stmt.setString(i,name);
                i++;
            }
            if (surname!=null && !surname.trim().isEmpty()){
                stmt.setString(i,name);
                i++;
            }
            if (nationalityId!=null){
                stmt.setInt(i,nationalityId);
            }
            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                User u = getUser(rs);

                result.add(u);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        User result = null;
        try(Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement("select * from user where email=? and password=?");
            stmt.setString(1,email);
            stmt.setString(2,password);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                result = getUser(rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public boolean addUser(User u) {
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement("insert into user(name,surname,phone,email) values (?, ?, ?, ?)");
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getSurname());
            stmt.setString(3, u.getPhone());
            stmt.setString(4, u.getEmail());
            return stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateUser(User u) {
        try (Connection c = connect()) {
            PreparedStatement stmt = c.prepareStatement("update user set name=?, surname=?, phone=?, email=? where id=?");
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getSurname());
            stmt.setString(3, u.getPhone());
            stmt.setString(4, u.getEmail());
            stmt.setInt(5, u.getId());
            return stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean removeUser(int id) {
        try (Connection c = connect()) {
            Statement stmt = c.createStatement();
            return stmt.execute("delete from user where id = " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getById(int userId) {
        User result = null;

        try (Connection c = connect()) {

            Statement stmt = c.createStatement();
            stmt.execute("select \n" +
                    "\t\t  u.*," +
                    "\t\t\tn.nationality as nationality," +
                    "\t\t\tc.name as birthplace " +
                    "\t\t\tfrom user u" +
                    "\t\tleft join country n on u.nationality_id = n.id" +
                    "\t\tleft join country c on u.birthplace_id = c.id where u.id = " + userId);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {

                result = getUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
