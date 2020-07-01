package com.xmu.arac.draw;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

public class VertexArrayFloorLine {
    private final FloatBuffer floatBuffer;

    //Every center has 4 line,each line contain 2Vertex,which has 3 value
    //10*10 * 4 * 2 * 3 = 2400
    private final int modelVertexNumber = 10 * 10 * 4 * 2 * 3;

    public VertexArrayFloorLine(){
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
