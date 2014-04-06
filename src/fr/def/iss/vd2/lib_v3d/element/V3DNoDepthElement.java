// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dScene is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// V3dScene is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with V3dScene.  If not, see <http://www.gnu.org/licenses/>.

package fr.def.iss.vd2.lib_v3d.element;

import org.lwjgl.opengl.GL11;

import com.spaceagencies.i3d.scene.I3dCamera;
import com.spaceagencies.i3d.scene.element.I3dElement;

import fr.def.iss.vd2.lib_v3d.V3DContext;

/**
 *
 * @author fberto
 */
public class V3DNoDepthElement extends I3dElement {

    private I3dElement childElement = null;
    private V3DBoundingBox boundingBox = new V3DBoundingBox();
    
    public V3DNoDepthElement() {
    }

    public V3DNoDepthElement(I3dElement element) {
        childElement = element;
    }



    public void setElement(I3dElement element) {
        childElement = element;
    }


    @Override
    protected void doDisplay( I3dCamera camera) {
        if(childElement == null) {
            return;
        }
        GL11.glPushAttrib(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        childElement.display( camera);

        GL11.glPopAttrib();

    }

    @Override
    protected void doInit() {
        if(childElement == null) {
            return;
        }
        childElement.init();
    }

    @Override
    protected void doSelect( I3dCamera camera, long parentId) {
        if(childElement == null) {
            return;
        }
        childElement.select( camera, parentId);
    }

    @Override
    public V3DBoundingBox getBoundingBox() {
        if(childElement != null) {
            //System.err.println("V3DColorElement childElement: "+childElement);
            boundingBox.setSize(childElement.getBoundingBox().getSize().multiply(childElement.getScale()));
            boundingBox.setPosition(childElement.getBoundingBox().getPosition().add(childElement.getPosition()));
            boundingBox.setFlat(childElement.getBoundingBox().isFlat());
            boundingBox.setExist(childElement.getBoundingBox().isExist());
        }
        return boundingBox;
    }
    

}
