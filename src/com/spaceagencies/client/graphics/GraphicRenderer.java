package com.spaceagencies.client.graphics;

import com.spaceagencies.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;

public interface GraphicRenderer {

    V3DCameraBinding getCameraBinding();

    void init();

    void frame(Timestamp timestamp);


    void resetGui();

}
