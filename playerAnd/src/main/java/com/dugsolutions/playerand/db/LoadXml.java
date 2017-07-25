package com.dugsolutions.playerand.db;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.dugsolutions.playerand.data.RaceCreature;
import com.dugsolutions.playerand.data.RaceLocations;
import com.dugsolutions.playerand.data.RaceLocations.RaceLocation;
import com.dugsolutions.playerand.R;
import com.dugsolutions.playerand.data.RacePlayer;
import com.dugsolutions.playerand.data.SkillDesc;
import com.dugsolutions.playerand.util.Roll;

import org.xmlpull.v1.XmlPullParser;

import timber.log.Timber;

/**
 * Created by dug on 7/12/17.
 */

public class LoadXml {

    public static LoadXml getInstance() {
        return sInstance;
    }

    public static void Init(Context ctx) {
        if (sInstance == null) {
            new LoadXml(ctx);
        }
    }

    static LoadXml sInstance;
    Context ctx;

    LoadXml(Context ctx) {
        sInstance = this;
        this.ctx = ctx;
    }

    public static void Load(Context ctx) {
        Init(ctx);
        sInstance.load();
    }

    void load() {
//        load(R.xml.creatures);
        load(R.xml.player);
//        load(R.xml.combat);
//        load(R.xml.culture);
//        load(R.xml.equipment);
//        load(R.xml.skills);
//        load(R.xml.spells);
    }

    void load(int fileResId) {
        try {
            XmlResourceParser parser   = ctx.getResources().getXml(fileResId);
            int               eventType;
            String            name;
            RaceCreature      creature = null;
            RacePlayer        player;
            SkillDesc         skill    = null;

            while (true) {
                parser.next();
                eventType = parser.getEventType();
                name = parser.getName();

                if (eventType == XmlPullParser.END_DOCUMENT) {
                    break;
                }
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    continue;
                }
                if (eventType == XmlPullParser.START_TAG) {
                    if ("creature".equals(name)) {
                        creature = null;
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("name".equals(attName)) {
                                String value = parser.getAttributeValue(i);
                                creature = TableRaceCreatures.getInstance().query(creature, value);
                            }
                        }
                        if (creature == null) {
                            Timber.e("Missing creature name");
                            break;
                        }
                        parseAttributes(parser, creature);
                    } else if ("player".equals(name)) {
                        player = null;
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("name".equals(attName)) {
                                String value = parser.getAttributeValue(i);
                                player = TableRaceCreatures.getInstance().queryPlayer(value);
                            }
                        }
                        if (player == null) {
                            Timber.e("Missing player race name");
                        }
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("creature".equals(attName)) {
                                String value = parser.getAttributeValue(i);
                                RaceCreature other = TableRaceCreatures.getInstance().query(value);
                                player.copy(other);
                            }
                        }
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("silver".equals(attName)) {
                                String value = parser.getAttributeValue(i);
                                player.silver = new Roll(value);
                            } else if ("help".equals(attName)) {
                                String value = parser.getAttributeValue(i);
                                player.help = value;
                            }
                        }
                        parseAttributes(parser, player);
                        creature = player;
                    } else if ("locations".equals(name)) {
                        if (creature == null) {
                            Timber.e("Floating locations not allowed");
                            break;
                        }
                        creature.locations = new RaceLocations();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("base".equals(attName)) {
                                creature.locations.baseExpr = parser.getAttributeValue(i);
                            }
                        }
                    } else if ("location".equals(name)) {
                        RaceLocation location = new RaceLocation();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("name".equals(attName)) {
                                String value = parser.getAttributeValue(i);
                                location.name = value;
                            } else if ("hp".equals(attName)) {
                                String value = parser.getAttributeValue(i);
                                location.hpExpr = value;
                            } else if ("roll".equals(attName)) {
                                location.roll = (short) parser.getAttributeIntValue(i, 0);
                            }
                        }
                        creature.locations.add(location);
                    } else if ("skill".equals(name)) {
                        skill = null;
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("name".equals(attName)) {
                                String value = parser.getAttributeValue(i);
                                skill = TableSkillDesc.getInstance().query(skill, value);
                            }
                        }
                        if (skill == null) {
                            Timber.e("Missing skill name");
                        }
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("chars".equals(attName)) {
                                skill.base = parser.getAttributeValue(i);
                            } else if ("parent".equals(attName)) {
                                String parentName = parser.getAttributeValue(i);
                                skill.parent = TableSkillDesc.getInstance().query(parentName);
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    Timber.d("MYDEBUG END TAG " + name);
                    if ("creature".equals(name) || "player".equals(name)) {
                        TableRaceCreatures.getInstance().store(creature);
                        creature = null;
                    } else if ("skill".equals(name)) {
                        TableSkillDesc.getInstance().store(skill);
                        skill = null;
                    } else if ("locations".equals(name)) {
                        creature.locations.sort();
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if (skill != null) {
                        skill.desc = parser.getText();
                    }
                }
            }
            parser.close();
        } catch (Exception ex) {
            Timber.e(ex);
        }
    }

    void parseAttributes(XmlResourceParser parser, RaceCreature creature) {
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            String attName = parser.getAttributeName(i);
            if ("str".equals(attName)) {
                creature.str = new Roll(parser.getAttributeValue(i));
            } else if ("con".equals(attName)) {
                creature.con = new Roll(parser.getAttributeValue(i));
            } else if ("siz".equals(attName)) {
                creature.siz = new Roll(parser.getAttributeValue(i));
            } else if ("dex".equals(attName)) {
                creature.dex = new Roll(parser.getAttributeValue(i));
            } else if ("ken".equals(attName) || "pow".equals(attName)) {
                creature.pow = new Roll(parser.getAttributeValue(i));
            } else if ("cha".equals(attName)) {
                creature.cha = new Roll(parser.getAttributeValue(i));
            } else if ("ins".equals(attName) || "int".equals(attName)) {
                creature.ins = new Roll(parser.getAttributeValue(i));
            } else if ("strikeRank".equals(attName)) {
                creature.strikeRank = (short) parser.getAttributeIntValue(i, 0);
            } else if ("move".equals(attName)) {
                creature.move = (short) parser.getAttributeIntValue(i, 0);
            }
        }
    }
}
