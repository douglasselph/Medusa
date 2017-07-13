package com.dugsolutions.playerand.db;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.dugsolutions.playerand.data.RaceCreature;
import com.dugsolutions.playerand.data.RaceLocations;
import com.dugsolutions.playerand.data.RaceLocations.RaceLocation;
import com.dugsolutions.playerand.R;
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
        load(R.xml.player);
    }

    void load(int fileResId) {
        try {
            XmlResourceParser parser = ctx.getResources().getXml(fileResId);
            int eventType;
            String name;
            RaceCreature creature = null;

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
                    if ("creature".equals(name) || "player".equals(name)) {
                        creature = new RaceCreature();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("name".equals(attName)) {
                                TableRaceCreatures.getInstance().query(creature, name);
                                creature.name = parser.getAttributeValue(i);
                            } else if ("str".equals(attName)) {
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
                            }
                        }
                        creature.isPlayer = "player".equals(name);
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
                                location.name = parser.getAttributeValue(i);
                            } else if ("hp".equals(attName)) {
                                location.hpExpr = parser.getAttributeValue(i);
                            } else if ("roll".equals(attName)) {
                                location.roll = (short) parser.getAttributeIntValue(i, 0);
                            }
                        }
                        creature.locations.add(location);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("creature".equals(name)) {
                        creature.locations.sort();
                        TableRaceCreatures.getInstance().store(creature);
                    } else if ("player".equals(name)) {

                    }
                } else if (eventType == XmlPullParser.TEXT) {

                }

            }
        } catch (Exception ex) {

        }
    }

}
