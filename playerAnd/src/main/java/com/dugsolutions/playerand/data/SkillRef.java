package com.dugsolutions.playerand.data;

import com.dugsolutions.playerand.db.TableSkillDesc;

/**
 * Created by dug on 7/24/17.
 */

public class SkillRef {
    public long  id;
    public long  skill_desc_id;
    public short value; // current specific skill value %

    public SkillDesc getDesc() {
        return TableSkillDesc.getInstance().query(skill_desc_id);
    }
}
