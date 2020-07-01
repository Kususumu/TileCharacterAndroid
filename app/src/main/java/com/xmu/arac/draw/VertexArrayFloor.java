package com.xmu.arac.draw;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

public class VertexArrayFloor{

    private final FloatBuffer floatBuffer;

    //The max of the floor is 10*10 = 100 and each center has 6 vertex which contain 3 values
    //so the max Number of float is 10*10*6*3 = 1800
    private final int modelVertexNumber = 7000;

    public VertexArrayFloor(){
        floatBuffer = ByteBuffer.allocateDirect(modelVertexNumber * Constants.BYTES_PER_FLOAT)
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

    public void pushDataToFloatBuffer(float[] t){
        floatBuffer.put(t);
    }
}
