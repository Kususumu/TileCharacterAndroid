package com.xmu.arac.shaderProgram;

import android.content.Context;

import com.xmu.arac.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class ColorShaderProgram extends ShaderProgram {
    //Uniform Locations
    //Matrix is not set now
    private final int uMatrixLocation;

    //Attribute Location
    private final int aPositionLocation;

    //Define uColorLocation
    private final int uColorLocation;

    public ColorShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader,
                R.raw.simple_fragment_shader);

        //Retrieve uniform locations for the shader program
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);

        aPositionLocation = glGetAttribLocation(program,A_POSITION);

        uColorLocation = glGetUniformLocation(program,U_COLOR);
    }

    public void setUniforms(float[] matrix , float r, float g, float b , float w){
        //Pass the matrix int the shader program
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);

        //Set Uniform Color
        // w = 1.0 means you ca see the whole color
        glUniform4f(uColorLocation,r,g,b,w);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

}
