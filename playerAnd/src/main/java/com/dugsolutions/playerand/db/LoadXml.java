package com.dugsolutions.playerand.db;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.dugsolutions.playerand.data.RaceCreature;
import com.dugsolutions.playerand.data.RaceLocations;
import com.dugsolutions.playerand.data.RaceLocations.RaceLocation;
import com.dugsolutions.playerand.R;

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
            RaceCreature animal = null;

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
                        animal = new RaceCreature();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("name".equals(attName)) {
                                animal.name = parser.getAttributeValue(i);
                            }
                        }
                    } else if ("locations".equals(name)) {
                        if (animal == null) {
                            Timber.e("Floating locations not allowed");
                            break;
                        }
                        animal.locations = new RaceLocations();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("base".equals(attName)) {
                                animal.locations.baseExpr = parser.getAttributeValue(i);
                            }
                        }
                    } else if ("location".equals(name)) {
                        RaceLocation location = new RaceLocation();
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            String attName = parser.getAttributeName(i);
                            if ("name".equals(attName)) {
                                location.name = parser.getAttributeValue(i);
                            } else if ("hp".equals(attName)) {
                                location.expr = parser.getAttributeValue(i);
                            } else if ("roll".equals(attName)) {
                                location.roll = (short) parser.getAttributeIntValue(i, 0);
                            }
                        }
                        animal.locations.add(location);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("creature".equals(name)) {
                        TableCreature.getInstance().store(animal);
                    }
                } else if (eventType == XmlPullParser.TEXT) {

                }

            }
        } catch (Exception ex) {

        }
    }

}
