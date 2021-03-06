package com.xmu.arac.shaderProgram;

import android.content.Context;

import com.xmu.arac.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class TextureShaderProgram extends ShaderProgram {
    //Uniform Locations
    //Matrix is not set now
    private final int uMatrixLocation;

    private final int uTextureUnitLocation;

    //Attribute Location
    private final int aPositionLocation;

    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context){
        super(context, R.raw.texture_vertex_shader,
                R.raw.texture_fragment_shader);

        //Retrieve uniform locations for the shader program
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);

        uTextureUnitLocation = glGetUniformLocation(program,U_TEXTURE_UNIT);

        aPositionLocation = glGetAttribLocation(program,A_POSITION);

        aTextureCoordinatesLocation =
                glGetAttribLocation(program,A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix , int textureId){
        //Pass the matrix int the shader program
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);

        glActiveTexture(GL_TEXTURE0);

        glBindTexture(GL_TEXTURE_2D,textureId);

        glUniform1i(uTextureUnitLocation,0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation(){
        return aTextureCoordinatesLocation;
    }
}
