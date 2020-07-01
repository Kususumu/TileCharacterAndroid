package com.xmu.arac.draw;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

public class VertexArrayTouchPoint {

    private final FloatBuffer floatBuffer;

    public VertexArrayTouchPoint(){
        floatBuffer = ByteBuffer.allocateDirect(6 * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
    }


    public void setVertexAttribPointer(int dataOffset,int attributeLocation,
                                       int componentCount,int stride){
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation,componentCount,GL_FLOAT,
                false,stride,floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }

    public void pushDataToFloatBuffer(float t){
        floatBuffer.put(t);
    }
}
