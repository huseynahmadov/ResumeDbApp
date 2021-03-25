package com.company.dao.impl;

import com.company.dao.inter.AbstractDAO;
import com.company.dao.inter.UserSkillDaoInter;
import com.company.entity.UserSkill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserSkillDaoImpl extends AbstractDAO implements UserSkillDaoInter {

    private UserSkill getUserSkill(ResultSet rs) throws Exception {
        int userId = rs.getInt("id");
        int skillId = rs.getInt("skill_id");
        String skillName = rs.getString("skill_name");
        int power = rs.getInt("power");

//        return new UserSkill(userId, new User(userId), new Skill(skillId, skillName), power);
        return null;
    }

    @Override
    public List<UserSkill> getAllSkillByUserId(int userId) {
        List<UserSkill> result = new ArrayList<>();

        try (Connection c = connect()) {

            PreparedStatement stmt = c.prepareStatement("SELECT\n"
                    + "\tu.*,\n"
                    + "\tus.skill_id,\n"
                    + "\ts.name AS skill_name,\n"
                    + "\tus.power \n"
                    + "FROM\n"
                    + "\tuser_skill us\n"
                    + "\tLEFT JOIN USER u ON us.user_id = u.id\n"
                    + "  LEFT JOIN skill s ON us.skill_id = s.id\n"
                    + "\n"
                    + "WHERE\n"
                    + "\tus.user_id = ?");
            stmt.setInt(1, userId);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                UserSkill u = getUserSkill(rs);
                result.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
